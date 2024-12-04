package com.rtlink.androidapp.webIO

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.TypefaceCompatUtil.getTempFile
import androidx.core.view.ViewCompat.getRotation
import com.google.android.gms.cast.framework.media.ImagePicker
import com.rtlink.androidapp.utils.RequirePermission
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Collections.rotate
import java.util.Date
import java.util.Locale
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
class TakePhoto(act: ComponentActivity, webV: WebView?) {

    // 选择的上传文件（单选或相机拍摄）
    private lateinit var currentPhotoUri: Uri
//    private var photoLauncher: ActivityResultLauncher<Intent>

    private val activity: ComponentActivity = act
    private val webView: WebView? = webV

    init {
//
//        // 注册相机结果监听器
//        photoLauncher =
//            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    // 将拍照所得变为base64字符串
//                    val targetImageUri = result.data?.data
//                    val bitmap: Bitmap = BitmapFactory.decodeFile(targetImageUri.toString())
//
//                    val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//                    val byteArray = byteArrayOutputStream.toByteArray()
//
//                    val encodedStr: String = Base64.encode(byteArray)
//
//                    println(" ------------------------------------ encodedStr $encodedStr ")
//                }
//            }

    }

    fun prepareToStart() {
    }

//    // 启用相机
//    private fun launchCamera() {
//        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        currentPhotoUri = createImageFileUri()
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
//        photoLauncher.launch(captureIntent)
//    }

//    // 为即将拍下的照片创建存储Uri???
//    private fun createImageFileUri(): Uri {
//        val fileName: String =
//            SimpleDateFormat("YYYYMMDD_HHmmss", Locale.getDefault()).format(Date()) + ".jpg"
//        val contentValues: ContentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
//            }
//        }
//        val resolver: ContentResolver = activity.contentResolver
//        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        return imageUri ?: throw RuntimeException("ImageUri is null")
//    }
}
