package com.rtlink.androidapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity

class ComButtonActivity : ComponentActivity() {

    private var currentToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 显示注册的页面
        setContentView(R.layout.com_button)

        // 设置不可点击的按钮
        registerDisabledBtn(R.id.button6)
        registerDisabledBtn(R.id.button7)

        // 绑定按钮点击事件
        registerOnBtnclickEvent(R.id.button0, "您点击了朴素按钮")
        registerOnBtnclickEvent(R.id.button1, "您点击了主要按钮")
        registerOnBtnclickEvent(R.id.button2, "您点击了成功按钮")
        registerOnBtnclickEvent(R.id.button3, "您点击了默认按钮")
        registerOnBtnclickEvent(R.id.button4, "您点击了危险按钮")
        registerOnBtnclickEvent(R.id.button5, "您点击了警告按钮")
        registerOnImgBtnclickEvent(R.id.imageButton1, "您点击了图片按钮")

    }

    // 将指定id的按钮注册为不可点击的按钮
    private fun registerDisabledBtn(button: Int) {
        val btn = findViewById<Button>(button)
        btn.setEnabled(false)
    }

    // 注册按扭点击事件
    private fun registerOnBtnclickEvent(btn: Int, str: String) {
        val btnEl = findViewById<Button>(btn)
        btnEl.setOnClickListener {
            currentToast?.cancel()
            currentToast = Toast.makeText(this, str, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }

    // 注册按扭点击事件
    private fun registerOnImgBtnclickEvent(btn: Int, str: String) {
        val btnEl = findViewById<ImageButton>(btn)
        btnEl.setOnClickListener {
            currentToast?.cancel()
            currentToast = Toast.makeText(this, str, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }

}
