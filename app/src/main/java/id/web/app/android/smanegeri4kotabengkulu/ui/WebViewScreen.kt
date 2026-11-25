package id.web.app.android.smanegeri4kotabengkulu.ui

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import id.web.app.android.smanegeri4kotabengkulu.MainActivity
import id.web.app.android.smanegeri4kotabengkulu.bridge.WebAppInterface
import id.web.app.android.smanegeri4kotabengkulu.security.CryptoUtils

// Decrypted User-Agent suffix appended to WebView: "dkg-cbt-sekolahkita.co.id"
private const val ENCRYPTED_USER_AGENT_SUFFIX =
    "cSIBhkPd+ijjFdE6Jr1aqA==:hL1viNGE3BzugL+8WQvjZa95FZorjAn2SzkP9TKaRSs="

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    activity: MainActivity,
    @Suppress("UNUSED_PARAMETER") onPinningApp: () -> Unit,
    url: String,
    onWebViewCreated: (WebView) -> Unit
) {
    @Suppress("UNUSED_VALUE", "ASSIGNED_VALUE_IS_NEVER_READ")
    var webView: WebView? = null
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    val userAgentSuffix = CryptoUtils.decrypt(ENCRYPTED_USER_AGENT_SUFFIX)
                    settings.userAgentString = "${settings.userAgentString} $userAgentSuffix"
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                    addJavascriptInterface(WebAppInterface(activity), "JSBridge")
                    loadUrl(url)
                    onWebViewCreated(this)
                    webView = this
                }
            },
            modifier = Modifier.weight(2f)
        )
    }
}
