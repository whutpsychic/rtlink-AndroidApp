package com.rtlink.androidapp.webIO

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import com.rtlink.androidapp.R

class ModalProgress(title: String, activity: ComponentActivity) {
    private val factory: LayoutInflater = LayoutInflater.from(activity)

    @SuppressLint("InflateParams")
    private val progressBarView: View = factory.inflate(R.layout.view_progress, null)

    val progressBar: ProgressBar = progressBarView.findViewById<ProgressBar>(R.id.pb)

    val dialog: AlertDialog =
        AlertDialog.Builder(activity).setIcon(R.mipmap.ic_launcher).setTitle(title)
            .setCancelable(false)
            .setView(progressBarView)
            .create()
}
