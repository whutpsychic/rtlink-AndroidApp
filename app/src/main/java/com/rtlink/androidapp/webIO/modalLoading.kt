package com.rtlink.androidapp.webIO

import android.app.ProgressDialog
import androidx.activity.ComponentActivity

fun modalLoadingFn(title: String, content: String, activity: ComponentActivity): ProgressDialog? {
    return ProgressDialog.show(activity, title, content)
}
