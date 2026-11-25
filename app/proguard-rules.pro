# Add project specific ProGuard rules here.
# Keep @JavascriptInterface methods accessible to the WebView JavaScript bridge.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
