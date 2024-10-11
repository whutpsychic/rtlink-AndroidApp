package com.rtlink.androidapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.rtlink.androidapp.webIO.Toast

class WebViewActivity : ComponentActivity() {

    // webView 显示的网址
    private val URL = "http://192.168.1.71:8088/"

    // finish函数触发次数优化（使之仅触发一次）
    private var finishedAlready: Boolean = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
        enableEdgeToEdge()
        // 显示注册的页面
        setContentView(R.layout.activity_webview)

        val webView = findViewById<WebView>(R.id.webView)
        // 注意要启用JS，默认是不启用的，否则将导致某些页面无法显示
        webView.settings.javaScriptEnabled = true
        // 启用此行代码可显示原生web端的一些功能比如：显示alert()
        webView.webChromeClient = WebChromeClient()
        // 生命周期监测
        webView.webViewClient = object : WebViewClient() {
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

        webView.loadUrl(URL)
        // 给webJS端安装功能
        installJsFns(webView)
    }

    // 给webJS端安装功能
    private fun installJsFns(webView: WebView) {
        webView.addJavascriptInterface(Toast(this), GlobalConfig.IOName)
    }


}
