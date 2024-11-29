package com.rtlink.androidapp.utils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.reflect.KFunction0

class RequirePermission(activity: ComponentActivity, permission: String, fn: KFunction0<Unit>) {

    companion object {
        // 文件选择
        const val FILE_CHOOSER_REQUEST_CODE = 1

        // 相机
        const val CAMERA_PERMISSION_REQUEST_CODE = 2
    }

    // 初始化时执行
    init {
        // 如果有该权限则执行后续函数
        if (
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fn()
        }
        // 否则请求该权限
        else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                getCodeByPermissionType(permission)
            )
        }
    }

    private fun getCodeByPermissionType(permission: String): Int {
        return when (permission) {
            android.Manifest.permission.CAMERA -> CAMERA_PERMISSION_REQUEST_CODE
            else -> 0
        }
    }

}
