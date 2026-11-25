package id.web.app.android.smanegeri4kotabengkulu.security

/**
 * Pure device-environment and forbidden-package definitions matching the APK checks.
 */
object DeviceCheckUtils {

    const val PLAY_STORE_PACKAGE = "com.android.vending"

    /**
     * Package names of floating, multi-window, split-screen, and dual-space apps
     * checked by JSBridge.isForbidden(), preserving the exact list from the APK.
     */
    val FORBIDDEN_PACKAGES: List<String> = listOf(
        "com.clone.android.dual.space",
        "com.lwi.android.flapps",
        "com.lwi.android.flappsfull",
        "com.jsvmsoft.stickynotes",
        "net.geekstools.floatshort.PRO",
        "com.glgjing.floating.apps.assistive.touch.pro",
        "com.jsvmsoft.stickynotes",
        "com.github.ericytsang.multiwindow.app.android",
        "com.split.screen.shortcut.overview.accessibility.notification",
        "any.splitscreen",
        "comspli.exaspli.splitscspli",
        "net.geekstools.floatshort.PRO",
        "com.dvg.multivideoplayer",
        "com.fb.splitscreenlauncher",
        "com.excelliance.multiaccounts",
        "com.dualbrowser.splitscreen.multi",
        "com.ccssoftwareinc.splitscreenlauncher",
        "com.clone.android.dual.space",
        "com.pspace.vandroid"
    )

    val QEMU_FILES: List<String> = listOf(
        "/dev/socket/qemud",
        "/dev/qemu_pipe",
        "/system/lib/libc_malloc_debug_qemu.so",
        "/sys/qemu_trace",
        "/system/bin/qemu-props"
    )

    /**
     * Evaluates emulator heuristics and Play Store presence.
     * Preserves the exact boolean semantics of the original APK where true indicates
     * an emulator indicator matched or Play Store is missing, and false indicates
     * a physical device with Play Store installed.
     */
    fun isEmulatorOrMissingPlayStore(
        fingerprint: String,
        model: String,
        manufacturer: String,
        host: String,
        brand: String,
        device: String,
        product: String,
        hardware: String,
        networkOperatorName: String?,
        fileExists: (String) -> Boolean,
        hasPlayStore: Boolean
    ): Boolean {
        val hasEmulatorBuildProps =
            fingerprint.startsWith("google/sdk_gphone_") ||
                fingerprint.startsWith("generic") ||
                fingerprint.startsWith("unknown") ||
                model.contains("Emulator") ||
                model.contains("Android SDK built for") ||
                manufacturer.contains("Genymotion") ||
                host.startsWith("Build") ||
                (brand.startsWith("generic") && device.startsWith("generic")) ||
                product == "google_sdk" ||
                networkOperatorName == "Android" ||
                networkOperatorName == "Unknown" ||
                hardware.contains("goldfish") ||
                hardware.contains("ranchu") ||
                product.contains("sdk") ||
                product.contains("google_sdk") ||
                product.contains("sdk_x86") ||
                product.contains("sdk_gphone64")

        if (hasEmulatorBuildProps) {
            return true
        }

        if (QEMU_FILES.any(fileExists)) {
            return true
        }

        return !hasPlayStore
    }
}
