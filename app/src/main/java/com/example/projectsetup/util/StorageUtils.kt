package com.example.projectsetup.util;

import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class StorageUtils {

    @WorkerThread
    suspend fun saveBitmapToFile(bitmap: Bitmap, filePath: String): String? {
        var path: String? = null
        try {
            FileOutputStream(File(filePath)).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                path = filePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
            path = null
        }
        return path
    }

}