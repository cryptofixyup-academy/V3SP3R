package com.vesper.flipper.ai

/**
 * Shared command action definitions used across all AI providers.
 * These actions represent the complete set of operations available on the Flipper Zero
 * through the Vesper control interface.
 */
object CommandActions {
    val SUPPORTED_ACTIONS = listOf(
        "list_directory",
        "read_file",
        "write_file",
        "create_directory",
        "delete",
        "move",
        "rename",
        "copy",
        "get_device_info",
        "get_storage_info",
        "search_faphub",
        "install_faphub_app",
        "push_artifact",
        "execute_cli",
        "forge_payload",
        "search_resources",
        "browse_repo",
        "download_resource",
        "github_search",
        "list_vault",
        "run_runbook",
        "launch_app",
        "subghz_transmit",
        "ir_transmit",
        "nfc_emulate",
        "rfid_emulate",
        "ibutton_emulate",
        "badusb_execute",
        "ble_spam",
        "led_control",
        "vibro_control",
        "request_photo"
    )
}
