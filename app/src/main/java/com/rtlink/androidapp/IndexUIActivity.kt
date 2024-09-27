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
        setContentView(R.layout.index_ui)

        // 注册点击函数
        // 去往指定页面
        val pageBtn = this.findViewById<Button>(R.id.button)
        pageBtn.setOnClickListener {
            val buttonPage = Intent(this, ComButtonActivity::class.java)
            startActivity(buttonPage)
        }

    }
}