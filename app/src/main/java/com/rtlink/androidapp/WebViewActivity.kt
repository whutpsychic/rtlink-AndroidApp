package com.rtlink.androidapp

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.rtlink.androidapp.utils.checkPermissionBeforeDo
import com.rtlink.androidapp.utils.makeToast
import com.rtlink.androidapp.webIO.Index
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// *********************** 关于<input type="file" /> ***********************
// - 仅抬起相机ok
// - 相机或选择文件ok
// - 相机或选择文件（多选）ok
// - 解决在多选文件时进行了快捷选择（单选）后闪退的问题ok
// - 解决在多选文件模式下拍照后无法正常传输的问题ok
// - 所有类型的文件可上传ok
// - 解决打开文件选择器但不做选择退出后闪退的问题ok
// ************************************************************************
// ************************** 加载本地html(离线运行) *************************
// - createWebHashHistory + base: './'
// ************************************************************************
class WebViewActivity : ComponentActivity() {

    // webView 显示的网址
    private val URL = "http://192.168.0.2:8088"

    // webView 实例
    private var webView: WebView? = null

    // finish函数触发次数优化（使之仅触发一次）
    private var finishedAlready: Boolean = false

    // 临时存放文件选取回调函数
    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null

    companion object {
        private const val FILE_CHOOSER_REQUEST_CODE = 1
        private const val CAMERA_PERMISSION_REQUEST_CODE = 2

        // 单文件选择模式码
        private const val SINGLE_FILE_CHOOSER_CODE = 0

        // 多文件选择模式码
        private const val MULTI_FILE_CHOOSER_CODE = 1
    }

    // 文件上传模式（默认单选）
    // 0 = 单选 1 = 多选
    private var currentFileChooserMode: Int = SINGLE_FILE_CHOOSER_CODE

    // 选择的上传文件（单选或相机拍摄）
    private lateinit var currentPhotoUri: Uri

    // 劫持 webView 后退事件（未完成）
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        println(" =========================================================== ")
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

        // ******** 允许访问本地文件系统（加载本地.html/.css/.js等文件） ********
        webView?.settings?.allowFileAccess = true;
        webView?.settings?.allowFileAccessFromFileURLs = true;
        // **************************************************************

        // 启用此行代码可显示原生web端的一些功能比如：显示alert()
        webView?.webChromeClient = object : WebChromeClient() {

            // 补丁 - <input type="file" />
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                // 备份回调函数
                fileUploadCallback = filePathCallback

                // 如果是选择图片
                if (fileChooserParams?.acceptTypes?.contains("image/*") == true) {
                    // 如果是仅使用相机
                    if (fileChooserParams.isCaptureEnabled) {
                        prepareCamera()
                    }
                    // 否则询问是相机还是选择文件
                    else {
                        // 询问使用相机还是图片文件选择器
                        val dialog =
                            AlertDialog.Builder(this@WebViewActivity)
                                .setCancelable(false)
                                .setItems(R.array.webView_fileChooser) { _, index ->
                                    // 选择动作
                                    val items =
                                        resources.getStringArray(R.array.webView_fileChooser)
                                    // 使用相机
                                    if (index == 0 && items[index] == "相机") {
                                        // 选择了相机就该将mode改为单文件模式
                                        currentFileChooserMode = SINGLE_FILE_CHOOSER_CODE
                                        prepareCamera()
                                    }
                                    // 使用图片文件选择器
                                    else {
                                        prepareFileChooser(fileChooserParams.mode, "image/*")
                                    }
                                }
                                .create()
                        dialog.show()
                    }
                }

                // 如果是任意文件
                if (fileChooserParams?.acceptTypes?.contains("*/*") == true) {
                    // 则直接打开文件选择器
                    prepareFileChooser(fileChooserParams.mode, "*/*")
                }

                return true
            }

        }

        // 一般生命周期监测
        webView?.webViewClient = object : WebViewClient() {
            // press ctrl+o (cmd+o) to override more methods
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // error, can't connect to the page
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // page loading started
            }

            // 加载完成后
            override fun onPageFinished(view: WebView, url: String) {
                if (finishedAlready) {
                    return
                }
                // page loading finished
                finishedAlready = true

//                Handler().postDelayed(Runnable {
//                    // ******************* 向 webView 发出函数执行指令 *******************
//                    webView?.evaluateJavascript(
//                        "alert('这是一条由native端发起，向web端传送的指令')",
//                        null
//                    )
//                }, 3000)
            }
        }

        // 清除缓存
        webView?.clearCache(true)

        // 加载指定地址
        webView?.loadUrl(URL)
        // 加载本地html
//        webView?.loadUrl("file:///android_asset/index.html");
        // 给webJS端安装功能函数
        installJsFns(webView)
    }

    // 给webJS端安装功能
    private fun installJsFns(w: WebView?) {
        w?.addJavascriptInterface(Index(this@WebViewActivity, w), GlobalConfig.IOName)
    }

    // 监测请求权限后的回调函数
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 监听相机请求
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获得允许，启用相机
                launchCamera()
            } else {
                makeToast(this, "您没有获取相机权限")
            }
        }
    }

    // 准备启用相机
    // 判断一下是否已经具备了访问相机的权限
    private fun prepareCamera() {
        checkPermissionBeforeDo(
            this@WebViewActivity,
            android.Manifest.permission.CAMERA,
            CAMERA_PERMISSION_REQUEST_CODE,
            ::launchCamera
        )
    }

    // 启用相机
    private fun launchCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        currentPhotoUri = createImageFileUri()
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
        startActivityForResult(captureIntent, FILE_CHOOSER_REQUEST_CODE)
    }

    // 为即将拍下的照片创建存储Uri???
    private fun createImageFileUri(): Uri {
        val fileName: String =
            SimpleDateFormat("YYYYMMDD_HHmmss", Locale.getDefault()).format(Date()) + ".jpg"
        val contentValues: ContentValues = ContentValues().apply {
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

    // 准备使用文件选择器
    private fun prepareFileChooser(modeCode: Int, type: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = type
        // 默认单文件选择器
        // 如果是多文件选择器
        if (modeCode == MULTI_FILE_CHOOSER_CODE) {
            // 文件多选
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        // 记录一下当前模式（是否multiple模式）
        currentFileChooserMode = modeCode
        val chooserIntent = Intent.createChooser(intent, "选择文件")
        startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.activity.ComponentActivity"
        )
    )
    // 监听Activity运行结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 监听得到图片后的操作
        // 文件选择结果
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            // 如果为空，抛出错误
            if (fileUploadCallback == null) {
                throw RuntimeException("fileUploadCallback is null")
            }

            // 拍照或单选文件
            if (currentFileChooserMode == SINGLE_FILE_CHOOSER_CODE) {
                val results: Array<Uri>? = when {
                    resultCode == RESULT_OK && data?.data != null -> arrayOf(data.data!!)
                    resultCode == RESULT_OK -> arrayOf(currentPhotoUri)
                    else -> null
                }
                // web端获取结果
                fileUploadCallback?.onReceiveValue(results)
            }

            // 多选文件
            else {
                // 快捷操作补丁（单选一个文件）
                if (data?.data != null) {
                    fileUploadCallback?.onReceiveValue(arrayOf(data.data!!))
                } else if (data?.clipData != null) {
                    var uriArr = arrayOf<Uri>()
                    for (i in 0 until data.clipData!!.itemCount) {
                        val uri = data.clipData!!.getItemAt(i).uri
                        uriArr = uriArr.plus(uri)
                    }
                    fileUploadCallback?.onReceiveValue(uriArr)
                }
            }
            // 重置回调函数
            fileUploadCallback = null
        }

    }

}
