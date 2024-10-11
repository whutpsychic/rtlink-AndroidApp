package com.rtlink.androidapp.webIO

import android.content.Context
import android.webkit.JavascriptInterface
import com.rtlink.androidapp.utils.makeToast

class Toast(private val context: Context) {
    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        makeToast(context, toast)
    }
}