package com.rtlink.androidapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rtlink.androidapp.utils.makeToast
import com.rtlink.androidapp.webIO.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WebViewActivity : ComponentActivity() {

    // webView 显示的网址
    private val URL = "http://192.168.1.71:8088/"

    // webView 实例
    private var webView: WebView? = null

    // finish函数触发次数优化（使之仅触发一次）
    private var finishedAlready: Boolean = false

    //
    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null

    companion object {
        private const val FILE_CHOOSER_REQUEST_CODE = 1
        private const val CAMERA_PERMISSION_REQUEST_CODE = 2
    }

    private lateinit var currentPhotoUri: Uri

    // 劫持 webView 后退事件
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView != null) {
            if (webView!!.canGoBack()) webView!!.goBack() else super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
        enableEdgeToEdge()
        // 显示注册的页面
        setContentView(R.layout.activity_webview)

        webView = findViewById<WebView>(R.id.webView)
        // 注意要启用JS，默认是不启用的，否则将导致某些页面无法显示
        webView?.settings?.javaScriptEnabled = true

        webView?.settings?.domStorageEnabled = true;
        webView?.settings?.allowFileAccess = true;
        webView?.settings?.allowContentAccess = true;
        webView?.settings?.javaScriptCanOpenWindowsAutomatically = true
        webView?.settings?.mediaPlaybackRequiresUserGesture = false

        // 启用此行代码可显示原生web端的一些功能比如：显示alert()
        webView?.webChromeClient = object : WebChromeClient() {

            // <input type="file" /> 补丁
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                println("----------------------------------------------- onShowFileChooser ")

                fileUploadCallback?.onReceiveValue(null)
                // 备份回调函数
                fileUploadCallback = filePathCallback

                if (fileChooserParams?.acceptTypes?.contains("image/*") == true && fileChooserParams.isCaptureEnabled) {
                    // Launch camera
                    if (ContextCompat.checkSelfPermission(
                            this@WebViewActivity,
                            android.Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        launchCamera()
                    } else {
                        ActivityCompat.requestPermissions(
                            this@WebViewActivity,
                            arrayOf(android.Manifest.permission.CAMERA),
                            CAMERA_PERMISSION_REQUEST_CODE
                        )
                    }
                } else {
                    // Use file picker
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "image/*"
                    // 文件多选
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    val chooserIntent = Intent.createChooser(intent, "Choose File")
                    startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE)
                }

                return true
            }
        }

        // 生命周期监测
        webView?.webViewClient = object : WebViewClient() {
            // press ctrl+o (cmd+o) to override more methods
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // error, can't connect to the page
                println(" ------------------------------------------- Sorry, an error occurred!")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // page loading started
                println(" ------------------------------------------- Page Load Started!")
            }

            // 加载完成后
            override fun onPageFinished(view: WebView, url: String) {
                if (finishedAlready) {
                    return
                }
                // page loading finished
                println(" ------------------------------------------- Page Load Finished!")
                finishedAlready = true

//                Handler().postDelayed(Runnable {
//                    // ******************* 向 webView 发出函数执行指令 *******************
//                    webView.evaluateJavascript(
//                        "alert('这是一条由native端发起，向web端传送的指令')",
//                        null
//                    )
//                }, 3000)
            }


        }

        webView?.loadUrl(URL)
        // 给webJS端安装功能
        installJsFns(webView)
    }

    // 给webJS端安装功能
    private fun installJsFns(w: WebView?) {
        w?.addJavascriptInterface(Toast(this), GlobalConfig.IOName)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch camera
                launchCamera()
            } else {
                makeToast(this, "Camera permission denied")
            }
        }
    }

    private fun createImageFileUri(): Uri {
        val fileName =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) + ".jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }
        }
        val resolver: ContentResolver = contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return imageUri ?: throw RuntimeException("ImageUri is null")
    }

    private fun launchCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        currentPhotoUri = createImageFileUri()
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
        startActivityForResult(captureIntent, FILE_CHOOSER_REQUEST_CODE)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.activity.ComponentActivity"
        )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        // 监听得到图片后的操作
        println(" ------------------------------------------- onActivityResult!")
        println(requestCode)
        println(resultCode)
        println(Activity.RESULT_OK)
        println(data)
        var results: Array<Uri>? = null

        val dataString = data?.dataString
        if (dataString != null) {
            results = arrayOf(Uri.parse(dataString))
        }
        println(dataString)
        println(results)

        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (fileUploadCallback == null) {
                println(" ------------------------------------------- fileUploadCallback == null")
                super.onActivityResult(requestCode, resultCode, data)
                return
            }

            val results: Array<Uri>? = when {
                resultCode == RESULT_OK && data?.data != null -> arrayOf(data.data!!)
                resultCode == RESULT_OK -> arrayOf(currentPhotoUri)
                else -> null
            }

            println(" ------------------------------------------- results")
            println(results)

            fileUploadCallback?.onReceiveValue(results)
            fileUploadCallback = null
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        return
    }


}
