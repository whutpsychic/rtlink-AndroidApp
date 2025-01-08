package com.rtlink.androidapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.rtlink.androidapp.activities.WebViewActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
        enableEdgeToEdge()
        // 显示主入口页面
        setContentView(R.layout.activity_main)

        // =========================== 调试专用，直接干到当前开发页面 ===========================
        val currDev = Intent(this, WebViewActivity::class.java)
        startActivity(currDev)
        // =================================================================================
    }

}
