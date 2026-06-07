package com.vesper.flipper.ai

import com.vesper.flipper.security.RateLimiter
import javax.inject.Singleton

/**
 * Shared API rate limiter for all AI provider clients.
 * Enforces a global 30 requests per 60 seconds policy across all providers.
 */
@Singleton
class ApiRateLimiter : RateLimiter(maxRequests = 30, windowMs = 60_000)
