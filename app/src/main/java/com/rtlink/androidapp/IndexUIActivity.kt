package com.rtlink.androidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

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
            val buttonPage = Intent(this, ComButtonActivity::class.java)
            startActivity(buttonPage)
        }

        val popupPage = this.findViewById<Button>(R.id.popup)
        popupPage.setOnClickListener {
            val buttonPage = Intent(this, ComPopupActivity::class.java)
            startActivity(buttonPage)
        }

    }
}