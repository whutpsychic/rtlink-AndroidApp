package com.rtlink.androidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.Toolbar

class IndexUIActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
//        enableEdgeToEdge()
        // 显示注册的页面
        setContentView(R.layout.index_ui)
        // ------------------------------------- 初始化顶部条 -------------------------------------
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // 设置后退按钮
        toolbar.setNavigationIcon(R.drawable.backup)
        // 监听后退按钮点击事件
        toolbar.setNavigationOnClickListener() {
            this.finish()
        }
        // -------------------------------------------------------------------------------------
        // 注册点击函数
        // 去往指定页面
        val pageBtn = this.findViewById<Button>(R.id.button)
        pageBtn.setOnClickListener {
            val buttonPage = Intent(this, ComButtonActivity::class.java)
            startActivity(buttonPage)
        }

    }
}