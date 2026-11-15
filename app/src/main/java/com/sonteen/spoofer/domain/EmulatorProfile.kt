package com.sonteen.spoofer.domain

/** Property set applied when the function is enabled. Edit freely. */
object EmulatorProfile {
    val props: LinkedHashMap<String, String> = linkedMapOf(
        "ro.product.model" to "sdk_gphone64_x86_64",
        "ro.product.manufacturer" to "Google",
        "ro.product.brand" to "google",
        "ro.product.device" to "emu64xa",
        "ro.product.name" to "sdk_gphone64_x86_64",
        "ro.hardware" to "ranchu",
        "ro.kernel.qemu" to "1",
        "ro.build.characteristics" to "emulator"
    )
}
