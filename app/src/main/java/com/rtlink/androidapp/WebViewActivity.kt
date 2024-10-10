package com.rtlink.androidapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class WebViewActivity : ComponentActivity() {

//    private val URL = "http://192.168.1.71:8088/"
//    private val URL = "http://www.baidu.com"
//    private val URL = "http://www.sina.com.cn"
    private val URL = "http://www.tencent.com"
//    private val URL = "https://vdo.ai/apptest"

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
                Toast.makeText(baseContext, "Sorry, an error occurred", Toast.LENGTH_SHORT).show()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // page loading started
                Toast.makeText(baseContext, "Page Load Started", Toast.LENGTH_SHORT).show()

            }

            override fun onPageFinished(view: WebView, url: String) {
                // page loading finished
                Toast.makeText(baseContext, "Page Load Finished", Toast.LENGTH_SHORT).show()
            }
        }
        webView.loadUrl(URL)
    }
}
