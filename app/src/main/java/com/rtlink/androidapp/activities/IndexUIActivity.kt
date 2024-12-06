package com.rtlink.androidapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.rtlink.androidapp.R

class IndexUIActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
//        enableEdgeToEdge()
        // 显示注册的页面
        setContentView(R.layout.activity_index_ui)

        // 注册点击函数
        // 去往指定页面
        val btnPage = this.findViewById<Button>(R.id.button)
        btnPage.setOnClickListener {
            val targetPage = Intent(this, ComButtonActivity::class.java)
            startActivity(targetPage)
        }

        val popupPage = this.findViewById<Button>(R.id.popup)
        popupPage.setOnClickListener {
            val targetPage = Intent(this, ComPopupActivity::class.java)
            startActivity(targetPage)
        }

        val webViewPage = this.findViewById<Button>(R.id.webview)
        webViewPage.setOnClickListener {
            val targetPage = Intent(this, WebViewActivity::class.java)
            startActivity(targetPage)
        }

        val webViewIpConfigPage = this.findViewById<Button>(R.id.webview_ipconfig)
        webViewIpConfigPage.setOnClickListener {
            val targetPage = Intent(this, WebViewIPConfigActivity::class.java)
            startActivity(targetPage)
        }

    }
}