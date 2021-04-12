package com.payfort.fortpaymentsdk.utils

import android.webkit.WebView

object WebViewUtils {


    fun setupWebView(webView: WebView) {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.setAppCacheEnabled(false)
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.displayZoomControls = true
        settings.setSupportZoom(true)
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = true
    }
}