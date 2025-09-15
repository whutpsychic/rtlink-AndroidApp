package com.rtlink.androidapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rtlink.androidapp.activities.WebViewActivity

class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式渲染
        enableEdgeToEdge()
        // 显示主入口页面
        setContentView(R.layout.activity_main)

        val brand: String? = Build.BRAND

        // huawei
        if (brand == "WIKO" || brand == "HUAWEI") {
            // 请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                3
            )
            // 1.2s后直接前往页面
            Handler().postDelayed(Runnable {
                nextStep()
            }, 1200)
        }
        // 默认先请求权限后前往页面
        else {
            // 向用户索要通知权限
            // 如果有该权限则执行后续函数
            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                nextStep()
            }
            // 否则请求该权限
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    3
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 3) {
            // 检查权限是否被授予
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                println(" ----------------------------------- 用户允许，调用目标函数 ")
                nextStep()  // 用户允许，调用目标函数
            } else {
//                println(" ----------------------------------- 用户拒绝，处理拒绝逻辑 ")
                // 用户拒绝，处理拒绝逻辑
                nextStep()
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    // 用户拒绝但未勾选“不再询问”，可再次解释并请求权限
//                    showPermissionRationaleDialog()
                } else {
                    // 用户勾选“不再询问”，需引导用户到设置页手动开启
                    Toast.makeText(this, "请在设置中开启权限", Toast.LENGTH_SHORT).show()
//                    openAppSettings()
                }
            }
        }
    }

    private fun nextStep() {
        val currDev = Intent(this, WebViewActivity::class.java)
        startActivity(currDev)
    }

}
