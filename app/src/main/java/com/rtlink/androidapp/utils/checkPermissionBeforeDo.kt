// 创建一个快捷的短提示
package com.rtlink.androidapp.utils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.reflect.KFunction0

fun checkPermissionBeforeDo(
    activity: ComponentActivity,
    permission: String,
    requestCode: Int,
    fn: KFunction0<Unit>
) {
    // 如果有该权限则执行后续函数
    if (ContextCompat.checkSelfPermission(
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
            requestCode
        )
    }
}


