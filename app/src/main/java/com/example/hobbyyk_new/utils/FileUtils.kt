package com.example.hobbyyk_new.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val myFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream = FileOutputStream(myFile)

    inputStream.copyTo(outputStream)
    inputStream.close()
    outputStream.close()

    return myFile
}