package com.onmoim.core.util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.util.UUID

object FileUtil {
    suspend fun getTempImagesDir(context: Context): File {
        val file = File(context.filesDir, "temp/images")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    suspend fun removeTempImagesDir(context: Context) {
        val file = getTempImagesDir(context)
        if (file.exists()) {
            file.deleteRecursively()
        }
    }

    suspend fun createTempImageFile(context: Context, contentUri: Uri): File? {
        var tempFile: File? = null

        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(contentUri)

            inputStream?.use { input ->
                val fileName = UUID.randomUUID().toString()
                val fileExtension = contentResolver.getType(contentUri)?.let {
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(it)
                } ?: "jpeg"
                tempFile = File(getTempImagesDir(context), "${fileName}.${fileExtension}")
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            return tempFile
        } catch (e: Exception) {
            Log.e("FileUtil", "createTempImageFile error", e)
            return null
        }
    }
}