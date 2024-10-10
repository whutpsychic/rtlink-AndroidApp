package com.rtlink.androidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
        enableEdgeToEdge()
        // 显示主入口页面
        setContentView(R.layout.activity_main)
        // 注册点击函数
        val btnUI = this.findViewById<Button>(R.id.btn_ui)
        btnUI.setOnClickListener {
            enterToUIPage()
        }

        // =========================== 调试专用，直接干到当前开发页面 ===========================
        val currDev = Intent(this, ComPopupActivity::class.java)
        startActivity(currDev)
        // =================================================================================
    }

    // 进入UI目录页
    private fun enterToUIPage() {
        // 跳转到对应的目录页面
        val intent = Intent(this, IndexUIActivity::class.java)
        startActivity(intent)
    }
}


