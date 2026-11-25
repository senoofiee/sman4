package id.web.app.android.smanegeri4kotabengkulu.bridge

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.webkit.JavascriptInterface
import id.web.app.android.smanegeri4kotabengkulu.MainActivity
import id.web.app.android.smanegeri4kotabengkulu.security.CryptoUtils
import id.web.app.android.smanegeri4kotabengkulu.security.DeviceCheckUtils
import java.io.File

/**
 * JavaScript bridge exposed to the CBT WebView under the "JSBridge" global name.
 */
class WebAppInterface(private val activity: MainActivity) {

    @JavascriptInterface
    fun isDevice(): Boolean {
        val telephonyManager =
            activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperatorName = telephonyManager.networkOperatorName
        val hasPlayStore = isPackageInstalled(DeviceCheckUtils.PLAY_STORE_PACKAGE)

        return DeviceCheckUtils.isEmulatorOrMissingPlayStore(
            fingerprint = Build.FINGERPRINT,
            model = Build.MODEL,
            manufacturer = Build.MANUFACTURER,
            host = Build.HOST,
            brand = Build.BRAND,
            device = Build.DEVICE,
            product = Build.PRODUCT,
            hardware = Build.HARDWARE,
            networkOperatorName = networkOperatorName,
            fileExists = { path -> File(path).exists() },
            hasPlayStore = hasPlayStore
        )
    }

    @JavascriptInterface
    fun isForbidden(): Boolean {
        return DeviceCheckUtils.FORBIDDEN_PACKAGES.any { packageName ->
            isPackageInstalled(packageName)
        }
    }

    @JavascriptInterface
    fun isAppPinned(): Boolean {
        val activityManager =
            activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val lockTaskModeState = activityManager.lockTaskModeState
        println("Lock Task State: $lockTaskModeState")
        return lockTaskModeState == ActivityManager.LOCK_TASK_MODE_PINNED
    }

    @JavascriptInterface
    fun startPinningApp() {
        try {
            activity.startLockTask()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JavascriptInterface
    fun stopPinningApp() {
        try {
            activity.stopLockTask()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JavascriptInterface
    fun getAppSignatureHash(): String {
        return try {
            val packageManager = activity.packageManager
            val packageName = activity.packageName
            val signatureBytes: ByteArray? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                packageInfo.signingInfo?.apkContentsSigners?.firstOrNull()?.toByteArray()
            } else {
                @Suppress("DEPRECATION")
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
                @Suppress("DEPRECATION")
                packageInfo.signatures?.firstOrNull()?.toByteArray()
            }

            if (signatureBytes == null) {
                ""
            } else {
                CryptoUtils.sha256Hex(signatureBytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun isPackageInstalled(packageName: String): Boolean {
        return try {
            activity.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }
}
