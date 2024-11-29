package com.rtlink.androidapp.webIO

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.ComponentActivity
import com.rtlink.androidapp.GlobalConfig.Companion.RamName
import com.rtlink.androidapp.activities.ScanningActivity
import com.rtlink.androidapp.activities.WebViewActivity
import com.rtlink.androidapp.utils.makeToast
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.MODAL_PROGRESS
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.READ_LOCAL

class Index(private val activity: ComponentActivity, private val webView: WebView?) {

    private var modalLoading: ProgressDialog? = null
    private var modalProgress: ModalProgress? = null

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        makeToast(activity, toast)
    }

    /** Show modalTips from the web page  */
    @JavascriptInterface
    fun modalTips(title: String, content: String) {
        modalTipsFn(title, content, activity, webView)
    }

    /** Show a modalConfirm from the web page  */
    @JavascriptInterface
    fun modalConfirm(title: String, content: String) {
        modalConfirmFn(title, content, activity, webView)
    }

    /** Show a modalLoading from the web page  */
    @JavascriptInterface
    fun modalLoading(title: String, content: String) {
        modalLoading = modalLoadingFn(title, content, activity)
    }

    /** End a modalLoading from the web page  */
    @JavascriptInterface
    fun finish() {
        modalLoading?.dismiss()
    }

    /** Show a modalProgress from the web page  */
    @JavascriptInterface
    fun modalProgress(title: String) {
        modalProgress = ModalProgress(title, activity)
        modalProgress?.dialog?.show()
    }

    /** Set modalProgress number from the web page  */
    @JavascriptInterface
    fun setProgress(num: Int) {
        modalProgress?.progressBar?.setProgress(num, true)
        // 如果进度条读满
        if (num >= 100) {
            // 0.5s后触发动作
            Handler().postDelayed(Runnable {
                modalProgress?.dialog?.dismiss()
                activity.runOnUiThread {
                    webView?.evaluateJavascript("$RamName.callback.$MODAL_PROGRESS()", null)
                }
            }, 500)
        }
    }

    /** Jump to native page for configuring web ip  */
    @JavascriptInterface
    fun ipConfig() {
    }
    /**  ----------------------------------------------------------------------------- */
    /** Write local storage from web  */
    /** Work with SharedPreferences  */
    @JavascriptInterface
    fun writeLocal(key: String, value: String) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    /** Read local storage from web  */
    @JavascriptInterface
    fun readLocal(key: String) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue: String = ""
        val content = sharedPref.getString(key, defaultValue)

        activity.runOnUiThread {
            webView?.evaluateJavascript("$RamName.callback.$READ_LOCAL('$content')", null)
        }
    }

    /** Dial numbers to prepare a phone call  */
    @JavascriptInterface
    fun phoneCall() {
    }

    /** Scan qrcode or barcode */
    @JavascriptInterface
    fun scan() {
        val intent = Intent(activity, ScanningActivity::class.java)
        WebViewActivity.scanResultLauncher.launch(intent)
    }

    /** Check for network type  */
    @JavascriptInterface
    fun checkNetworkType() {
    }

    /** Get Safe Height  */
    @JavascriptInterface
    fun getSafeHeight() {
    }

    /** Go to take a photo  */
    @JavascriptInterface
    fun takePhoto() {
    }

    /** Vibrate Action  */
    @JavascriptInterface
    fun vibrate() {
    }

    /** Display a notification on top  */
    @JavascriptInterface
    fun notification() {
    }
}