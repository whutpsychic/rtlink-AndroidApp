package com.rtlink.androidapp.webIO

import android.app.AlertDialog
import android.webkit.WebView
import androidx.activity.ComponentActivity
import com.rtlink.androidapp.GlobalConfig.Companion.RamName
import com.rtlink.androidapp.R
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.modalTips

fun modalTipsFn(title: String, content: String, activity: ComponentActivity, webView: WebView?) {
    val dialog = AlertDialog.Builder(activity)
        .setIcon(R.mipmap.ic_launcher)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton("确定") { _, _ ->
            activity.runOnUiThread {
                webView?.evaluateJavascript("${RamName}.callback.${modalTips}()", null)
            }
        }
        .create()
    dialog.show()
}