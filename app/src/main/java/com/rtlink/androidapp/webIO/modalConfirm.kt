package com.rtlink.androidapp.webIO

import android.app.AlertDialog
import android.webkit.WebView
import androidx.activity.ComponentActivity
import com.rtlink.androidapp.GlobalConfig.Companion.RAM_NAME
import com.rtlink.androidapp.R
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.MODAL_CONFIRM

fun modalConfirmFn(title: String, content: String, activity: ComponentActivity, webView: WebView?) {
    val dialog = AlertDialog.Builder(activity)
        .setIcon(R.mipmap.ic_launcher)
        .setTitle(title)
        .setMessage(content)
        .setCancelable(false)
        .setNegativeButton("取消", null)
        .setPositiveButton("确定") { _, _ ->
            activity.runOnUiThread {
                webView?.evaluateJavascript("${RAM_NAME}.callback.${MODAL_CONFIRM}()", null)
            }
        }
        .create()
    dialog.show()
}