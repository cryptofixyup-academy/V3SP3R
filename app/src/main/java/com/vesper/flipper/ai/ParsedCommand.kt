package com.vesper.flipper.ai

import com.vesper.flipper.domain.model.ExecuteCommand

/**
 * Result of parsing AI-generated command text. Used by all AiClient implementations.
 */
data class ParsedCommand(
    val command: ExecuteCommand? = null,
    val error: String? = null
)
