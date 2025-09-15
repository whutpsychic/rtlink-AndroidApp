package com.rtlink.androidapp.webIO

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rtlink.androidapp.MainActivity
import com.rtlink.androidapp.activities.WebViewActivity.Companion.CHANNEL_ID
import me.leolin.shortcutbadger.ShortcutBadger

// 三方包内方法已知适用于 vivo
fun setBadgeNum(id: Int, activity: ComponentActivity, num: Int, fixedup: Boolean) {
    val context: Context = activity.baseContext
    val brand: String? = Build.BRAND
    val androidV: String = Build.VERSION.RELEASE
//    println(" -------------------------------------------------------------- setBadgeNum Build.BRAND ")
//    println(brand)
//    println(num)
//    println(androidV)

    // vivo
    if (brand == "vivo") {
        // 三方包内方法
        try {
            ShortcutBadger.applyCount(context, num)
        } catch (err: Error) {
            err.printStackTrace()
        }
    }
    // huawei
    else if (brand == "WIKO" || brand == "HUAWEI") {
        setHuaweiBadge(context, num)
    }
    // honor
    else if (brand == "HONOR") {
        setHonorBadge(context, num)
    }
    // 通用
    else {
        if (fixedup) {
            // 通用补丁：通过无显通知的方式设置角标数字
            try {
                doInvisibleNotifiedBadge(id, activity, num)
            } catch (err: Error) {
                err.printStackTrace()
            }
        }
    }

}

fun clearBadgeNum(id: Int, activity: ComponentActivity) {
    val context: Context = activity.baseContext
    val brand: String? = Build.BRAND
//    println(" -------------------------------------------------------------- clearBadgeNum ")

    if (brand == "vivo") {
        // 三方包内方法
        try {
            ShortcutBadger.removeCount(context)
        } catch (err: Error) {
            err.printStackTrace()
        }
    }
    // huawei
    else if (brand == "HUAWEI" || brand == "huawei") {
        setHuaweiBadge(context, 0)
    }
    // honor
    else if (brand == "HONOR" || brand == "honor") {
        setHonorBadge(context, 0)
    }
    // 通用
    else {
        // 通用补丁：通过无显通知的方式设置角标数字
        try {
            doInvisibleNotifiedBadge(id, activity, 0)
        } catch (err: Error) {
            err.printStackTrace()
        }
    }

}

// ============================== 通用 ==============================
// 已知适用于
// Xiaomi HyperOS/MIUI
private fun doInvisibleNotifiedBadge(id: Int, activity: ComponentActivity, num: Int) {
    val context: Context = activity.baseContext
    val notification = NotificationCompat.Builder(activity, CHANNEL_ID)
        // 通知图标
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        // 角标数字
        .setNumber(num)
        // 优先级
        .setPriority(NotificationCompat.PRIORITY_MIN)
        // 自动关闭
        .setAutoCancel(true).build()

    with(NotificationManagerCompat.from(activity)) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@with
        }
        // notificationId is a unique int for each notification that you must define.
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

// 华为角标
fun setHuaweiBadge(context: Context, count: Int) {
    try {
        val bundle = Bundle().apply {
            putString("package", context.packageName)
            putString("class", MainActivity::class.java.name) // 获取入口Activity类名
            putInt("badgenumber", count)
        }
        context.contentResolver.call(
            Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
            "change_badge", null, bundle
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// 荣耀角标
private fun setHonorBadge(context: Context, count: Int) {
    try {
        val bundle = Bundle().apply {
            putString("package", context.packageName)
            putString("class", MainActivity::class.java.name) // 获取入口Activity类名
            putInt("badgenumber", count)
        }
        context.contentResolver.call(
            Uri.parse("content://com.hihonor.android.launcher.settings/badge/"),
            "change_badge", null, bundle
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
