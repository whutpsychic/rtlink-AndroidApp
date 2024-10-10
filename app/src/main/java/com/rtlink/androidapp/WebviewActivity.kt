package com.rtlink.androidapp

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class WebviewActivity : ComponentActivity() {

    private val URL = "https://www.jianshu.com/p/c870b6313fff"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
        enableEdgeToEdge()
        // 显示注册的页面
        setContentView(R.layout.activity_webview)

        val webview = findViewById<WebView>(R.id.webview)
        webview.loadUrl(URL)
    }
}
