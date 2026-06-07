package com.vesper.flipper.ai

import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Shared HTTP request executor with exponential backoff retry logic.
 * Used by all AI provider clients to handle transient failures consistently.
 */
class HttpRetryExecutor(
    private val client: OkHttpClient,
    private val maxRetries: Int = 2,
    private val initialDelayMs: Long = 700L,
    private val maxDelayMs: Long = 10_000L,
    private val backoffMultiplier: Double = 2.0,
    private val tag: String = "HttpRetryExecutor"
) {
    /**
     * Execute HTTP request with exponential backoff retry for transient failures.
     *
     * @param request The OkHttp request to execute
     * @param parseResponse Callback to parse successful response body
     * @return Result with parsed response or error
     */
    suspend inline fun <reified T> execute(
        request: Request,
        crossinline parseResponse: (String) -> T,
        crossinline onError: (Int, String) -> T
    ): T {
        var lastException: Exception? = null
        var delayMs = initialDelayMs

        repeat(maxRetries) {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()

                    // Rate limit - wait and retry
                    if (response.code == 429) {
                        val retryAfter = response.header("retry-after")?.toLongOrNull() ?: 60
                        delay(retryAfter * 1000)
                        return@repeat
                    }

                    // Server errors (5xx) - retryable with backoff
                    if (response.code in 500..599) {
                        lastException = IOException("Server error: ${response.code}")
                        delay(delayMs)
                        delayMs = (delayMs * backoffMultiplier).toLong()
                            .coerceAtMost(maxDelayMs)
                        return@repeat
                    }

                    // Client errors (4xx except 429) - not retryable
                    if (!response.isSuccessful) {
                        Log.e(tag, "HTTP error ${response.code}: ${responseBody ?: "unknown"}")
                        return onError(response.code, responseBody ?: "unknown error")
                    }

                    if (responseBody == null) {
                        return onError(200, "empty response body")
                    }

                    return parseResponse(responseBody)
                }

            } catch (e: SocketTimeoutException) {
                lastException = e
                delay(delayMs)
                delayMs = (delayMs * backoffMultiplier).toLong()
                    .coerceAtMost(maxDelayMs)

            } catch (e: IOException) {
                lastException = e
                delay(delayMs)
                delayMs = (delayMs * backoffMultiplier).toLong()
                    .coerceAtMost(maxDelayMs)

            } catch (e: Exception) {
                // Non-retryable exception
                return onError(0, e.message ?: "unknown error")
            }
        }

        return onError(0, "failed after $maxRetries attempts: ${lastException?.message}")
    }
}
