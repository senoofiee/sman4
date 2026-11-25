package id.web.app.android.smanegeri4kotabengkulu

import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.web.app.android.smanegeri4kotabengkulu.security.CryptoUtils
import id.web.app.android.smanegeri4kotabengkulu.ui.WebViewScreen
import id.web.app.android.smanegeri4kotabengkulu.ui.theme.MyApplicationTheme

// Decrypted CBT exam portal URL: "https://cbt-smanpa.sch.id"
private const val ENCRYPTED_EXAM_URL =
    "VBOUc8EYjrUI/45zlIvaow==:CsxPkXCyc5azaG2oU5bW/N4AlvI3fBT7MqWZ1d8sR8Y="

class MainActivity : ComponentActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Block screenshots and screen recording while the exam activity is active.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContent {
            MyApplicationTheme {
                val examUrl = CryptoUtils.decrypt(ENCRYPTED_EXAM_URL)
                WebViewScreen(
                    activity = this@MainActivity,
                    onPinningApp = {
                        try {
                            startLockTask()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    url = examUrl,
                    onWebViewCreated = { createdWebView ->
                        webView = createdWebView
                    }
                )
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // Matches original APK behavior by invoking window.selesaiTest() on window focus events.
        webView.evaluateJavascript("window.selesaiTest();") {}
    }
}
