package com.rtlink.androidapp.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import com.rtlink.androidapp.R
import com.rtlink.androidapp.utils.makeToast

class ComPopupActivity : ComponentActivity() {

    private var locationIndex: Int = -1
    private var locationName: String = ""

    private var locationCheckList: BooleanArray =
        booleanArrayOf(false, false, false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 显示注册的页面
        setContentView(R.layout.activity_com_popup)

        val btn0 = findViewById<Button>(R.id.btn0)
        btn0.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("标题")
                .setMessage("一般的警告文本对话框")
                .setPositiveButton("确定") { _, _ ->
                    makeToast(this, "你点击了确定按钮")
                }
                .create()
            dialog.show()
        }

        val btn1 = findViewById<Button>(R.id.btn1)
        btn1.setOnClickListener {
            val dialog =
                AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("你确定吗？")
                    .setMessage("完整的确认对话框")
                    .setPositiveButton("确定") { _, _ ->
                        makeToast(this, "你点击了确定按钮")
                    }.setNegativeButton("取消") { _, _ ->
                        makeToast(this, "你点击了取消按钮")
                    }.setNeutralButton("其他") { _, _ ->
                        makeToast(this, "你点击了其他按钮")
                    }
                    .create()
            dialog.show()
        }

        val btn2 = findViewById<Button>(R.id.btn2)
        btn2.setOnClickListener {
            val dialog =
                AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("地区选择")
                    .setItems(R.array.Radio_dialog_items) { _, index ->
                        val items = resources.getStringArray(R.array.Radio_dialog_items)
                        makeToast(this, "你选择的位置是：${index}，你选择的洲是${items[index]}")
                    }
                    .create()
            dialog.show()
        }

        val btn3 = findViewById<Button>(R.id.btn3)
        btn3.setOnClickListener {
            val dialog =
                AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("地区选择")
                    .setSingleChoiceItems(R.array.Radio_dialog_items, locationIndex) { _, index ->
                        val items = resources.getStringArray(R.array.Radio_dialog_items)
                        locationIndex = index
                        locationName = items[index]
                    }
                    .setPositiveButton("确定") { _, _ ->
                        makeToast(
                            this,
                            "你选择的位置是：${locationIndex}，你选择的洲是${locationName}"
                        )
                    }.setNeutralButton("其他") { _, _ ->
                        makeToast(this, "你点击了其他按钮")
                    }.setNegativeButton("取消") { _, _ ->
                        makeToast(this, "你点击了取消按钮")
                    }
                    .create()
            dialog.show()
        }

        val btn4 = findViewById<Button>(R.id.btn4)
        btn4.setOnClickListener {
            val dialog =
                AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("地区选择")
                    .setMultiChoiceItems(
                        R.array.Radio_dialog_items,
                        locationCheckList
                    ) { _, which, isChecked -> locationCheckList[which] = isChecked }
                    .setPositiveButton("确定") { _, _ ->
                        val items = resources.getStringArray(R.array.Radio_dialog_items)
                        val selected = mutableListOf<String>()

                        for (idx in locationCheckList.indices) {
                            if (locationCheckList[idx]) {
                                selected.add(items[idx])
                            }
                        }
                        makeToast(
                            this,
                            "你选择的位置是：${selected}"
                        )
                    }.setNeutralButton("其他") { _, _ ->
                        makeToast(this, "你点击了其他按钮")
                    }.setNegativeButton("取消") { _, _ ->
                        makeToast(this, "你点击了取消按钮")
                    }
                    .create()
            dialog.show()
        }

        val btn5 = findViewById<Button>(R.id.btn5)
        btn5.setOnClickListener {
            val factory = LayoutInflater.from(this)
            val loginView: View = factory.inflate(R.layout.view_login, null)
            makeToast(this, "此对话框点击幕布不可关闭")
            val dialog =
                AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("自定义对话框")
                    .setCancelable(false)
                    .setView(loginView)
                    .setPositiveButton("登录") { _, _ ->
                        makeToast(this, "你点击了登录按钮")
                    }.setNegativeButton("取消") { _, _ ->
                        makeToast(this, "你点击了取消按钮")
                    }
                    .create()
            dialog.show()
        }
    }
}