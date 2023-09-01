package com.rookmotion.rookconnectdemo.extension

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

fun WebView.enableJavaScriptAndDomStorage() {
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
}

fun WebView.setOnNavigationRequestListener(block: (WebResourceRequest) -> Boolean) {
    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return if (request != null) {
                block(request)
            } else {
                false
            }
        }
    }
}
