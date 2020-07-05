package com.example.projectsetup.util

import android.os.Environment
import java.io.File

object EnvironmentUtil {

    private val internalStoragePath: String
        get() = Environment.getExternalStorageDirectory().toString()

    private val PARENT_FOLDER_PATH = "$internalStoragePath/Studenthelper"
    private val COMPRESSED_FOLDER_PATH = "$PARENT_FOLDER_PATH/compressed/"

    init {
        val file = File(COMPRESSED_FOLDER_PATH)
        file.mkdirs()
    }

    fun getCompressedImagesDirectory(): String {
        return COMPRESSED_FOLDER_PATH
    }

    fun getCroppedImageFilePath(): String {
        return "$PARENT_FOLDER_PATH/cropped.png"
    }

    fun getCroppedPageImageFilePath(): String {
        return "$PARENT_FOLDER_PATH/croppedPage.png"
    }

    fun getCroppedQuestionImageFilePath(): String {
        return "$PARENT_FOLDER_PATH/croppedQuestion.png"
    }

    fun getAnswerImageFilePath(): String {
        return "$PARENT_FOLDER_PATH/answer.png"
    }

    fun getCroppedAnswerImageFilePath(): String {
        return "$PARENT_FOLDER_PATH/croppedAnswer.png"
    }

    fun getDownloadQuestionFilePath(): String {
        return "$PARENT_FOLDER_PATH/sharedQuestion.jpg"
    }

}