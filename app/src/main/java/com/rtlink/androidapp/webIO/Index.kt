package com.rtlink.androidapp.webIO

import android.Manifest.*
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rtlink.androidapp.GlobalConfig.Companion.RAM_NAME
import com.rtlink.androidapp.R
import com.rtlink.androidapp.activities.ScanningActivity
import com.rtlink.androidapp.activities.WebViewActivity
import com.rtlink.androidapp.activities.WebViewActivity.Companion.CHANNEL_ID
import com.rtlink.androidapp.activities.WebViewIPConfigActivity
import com.rtlink.androidapp.utils.LocalStorage
import com.rtlink.androidapp.utils.makeToast
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.GET_SAFE_TOP
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.MODAL_LOADING
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.NETWORK_TYPE
import com.rtlink.androidapp.webIO.CallbackKeys.Companion.READ_LOCAL

class Index(private val activity: WebViewActivity, private val webView: WebView?) {

    // 正在加载
    private var modalLoading: ProgressDialog? = null

    // 进度条
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
        activity.runOnUiThread {
            webView?.evaluateJavascript("$RAM_NAME.callback.$MODAL_LOADING()", null)
        }
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
            }, 500)
        }
    }

    /** Jump to native page for configuring web ip  */
    @JavascriptInterface
    fun ipConfig() {
        val intent = Intent(activity, WebViewIPConfigActivity::class.java)
        activity.ipConfigLauncher.launch(intent)
    }
    /**  ----------------------------------------------------------------------------- */
    /** Write local storage from web  */
    /** Work with SharedPreferences  */
    @JavascriptInterface
    fun writeLocal(key: String, value: String) {
        val localStorage = LocalStorage(activity)
        localStorage.write(key, value)
    }

    /** Read local storage from web  */
    @JavascriptInterface
    fun readLocal(key: String) {
        val localStorage = LocalStorage(activity)
        val content = localStorage.read(key)
        activity.runOnUiThread {
            webView?.evaluateJavascript("$RAM_NAME.callback.$READ_LOCAL('$content')", null)
        }
    }

    /** Dial numbers to prepare a phone call  */
    @JavascriptInterface
    fun preDial(number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        activity.startActivity(intent)
    }

    /** Scan qrcode or barcode */
    @JavascriptInterface
    fun scan() {
        val intent = Intent(activity, ScanningActivity::class.java)
        activity.scanResultLauncher.launch(intent)
    }

    /** Check for network type  */
    @JavascriptInterface
    fun checkNetworkType() {
        val cm =
            activity.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val res = cm.getActiveNetworkInfo()?.typeName

        activity.runOnUiThread {
            webView?.evaluateJavascript("$RAM_NAME.callback.$NETWORK_TYPE('$res')", null)
        }
    }

    /** Get Safe Height  */
    @JavascriptInterface
    fun getSafeTop() {
        val windowInsets = activity.window.decorView.rootWindowInsets
        val top: Int = windowInsets.getInsets(1).top / 2
        activity.runOnUiThread {
            webView?.evaluateJavascript("$RAM_NAME.callback.$GET_SAFE_TOP($top)", null)
        }
    }

    /** Go to take a photo  */
    @JavascriptInterface
    fun takePhoto() {
        activity.prepareTakePhoto()
    }

    /** Vibrate Action  */
    @JavascriptInterface
    fun vibrate() {
        val vibe: Vibrator = activity.getSystemService("vibrator") as Vibrator
        vibe.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
    }

    // 强制横屏/恢复横屏
    @JavascriptInterface
    fun setScreenHorizontal() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    // 恢复竖屏/恢复竖屏
    @JavascriptInterface
    fun setScreenPortrait() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /** Display a notification on top  */
    @JavascriptInterface
    fun notification(id: Int, title: String, content: String) {
//        // Create an explicit intent for an Activity in your app.
//        val intent = Intent(activity, WebViewActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent =
//            PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(activity, CHANNEL_ID)
            .setSmallIcon(R.drawable.backup)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(activity)) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(id, builder.build())
        }
    }

    @JavascriptInterface
    fun notificationAsync(id: Int, title: String, content: String, seconds: Int) {
        Handler().postDelayed(Runnable {
            notification(id, title, content)
        }, (seconds * 1000).toLong())
    }

}