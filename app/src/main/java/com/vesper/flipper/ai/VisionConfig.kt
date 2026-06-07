package com.vesper.flipper.ai

/**
 * Shared vision system prompts and configuration used across all AI providers.
 */
object VisionConfig {
    const val VISION_SYSTEM_PROMPT =
        "You are a visual analysis assistant for a Flipper Zero companion app. " +
        "Describe what you see in the image in detail. Focus on: brand names, model numbers, " +
        "device types (TV, AC, car, remote control, gate, etc.), any visible text or labels, " +
        "and any details that would help identify the correct IR/RF/NFC protocol or signal. " +
        "Be specific and concise."
}
