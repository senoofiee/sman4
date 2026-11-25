package id.web.app.android.smanegeri4kotabengkulu.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceCheckUtilsTest {

    private fun checkDevice(
        fingerprint: String = "google/panther/panther:14/AP2A/123:user/release-keys",
        model: String = "Pixel 7",
        manufacturer: String = "Google",
        host: String = "abfarm-release",
        brand: String = "google",
        device: String = "panther",
        product: String = "panther",
        hardware: String = "panther",
        networkOperatorName: String? = "Telkomsel",
        existingFiles: Set<String> = emptySet(),
        hasPlayStore: Boolean = true
    ): Boolean {
        return DeviceCheckUtils.isEmulatorOrMissingPlayStore(
            fingerprint = fingerprint,
            model = model,
            manufacturer = manufacturer,
            host = host,
            brand = brand,
            device = device,
            product = product,
            hardware = hardware,
            networkOperatorName = networkOperatorName,
            fileExists = { path -> path in existingFiles },
            hasPlayStore = hasPlayStore
        )
    }

    @Test
    fun returnsFalseForPhysicalDeviceWithPlayStore() {
        assertFalse(checkDevice())
    }

    @Test
    fun returnsTrueWhenPlayStoreIsMissing() {
        assertTrue(checkDevice(hasPlayStore = false))
    }

    @Test
    fun returnsTrueWhenEmulatorIndicatorsMatch() {
        assertTrue(checkDevice(fingerprint = "google/sdk_gphone_x86/emu:14"))
        assertTrue(checkDevice(fingerprint = "generic/aosp"))
        assertTrue(checkDevice(fingerprint = "unknown/dev"))
        assertTrue(checkDevice(model = "Android Emulator"))
        assertTrue(checkDevice(model = "Android SDK built for x86"))
        assertTrue(checkDevice(manufacturer = "Genymotion"))
        assertTrue(checkDevice(host = "BuildHost01"))
        assertTrue(checkDevice(brand = "generic_x86", device = "generic_x86"))
        assertTrue(checkDevice(product = "google_sdk"))
        assertTrue(checkDevice(networkOperatorName = "Android"))
        assertTrue(checkDevice(networkOperatorName = "Unknown"))
        assertTrue(checkDevice(hardware = "goldfish"))
        assertTrue(checkDevice(hardware = "ranchu"))
        assertTrue(checkDevice(product = "sdk_gphone64_arm64"))
        assertTrue(checkDevice(existingFiles = setOf("/dev/qemu_pipe")))
    }

    @Test
    fun requiresBothBrandAndDeviceGenericForGenericBrandRule() {
        assertFalse(checkDevice(brand = "generic_custom", device = "panther"))
    }

    @Test
    fun forbiddenPackagesContainExpectedEntries() {
        assertEquals(19, DeviceCheckUtils.FORBIDDEN_PACKAGES.size)
        assertTrue("com.lwi.android.flapps" in DeviceCheckUtils.FORBIDDEN_PACKAGES)
        assertTrue("com.clone.android.dual.space" in DeviceCheckUtils.FORBIDDEN_PACKAGES)
        assertTrue("com.pspace.vandroid" in DeviceCheckUtils.FORBIDDEN_PACKAGES)
    }
}
