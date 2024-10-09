// 创建一个快捷的短提示
package com.rtlink.androidapp.utils

import android.content.Context
import android.widget.Toast

fun makeToast(ctx: Context, content: String) {
    Toast.makeText(ctx, content, Toast.LENGTH_SHORT).show()
}


