package com.rtlink.androidapp.webIO

import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.ComponentActivity
import com.rtlink.androidapp.utils.makeToast

class Index(private val activity: ComponentActivity, private val webView: WebView?) {
    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        makeToast(activity, toast)
    }

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun modalTips(title: String, content: String) {
        modalTipsFn(title, content, activity, webView)
    }

}