package com.example.projectsetup.util;

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MultipartUtils(val context: Context) {

    @WorkerThread
    suspend fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = FileUtils.getFile(context, fileUri)
        val requestFile = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                file
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}