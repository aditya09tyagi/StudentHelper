package com.example.projectsetup.ui.widget.photo_view_layout.common.extensions

import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView

fun ImageView.copyBitmapFrom(target: ImageView?) {
    target?.drawable?.let {
        if (it is BitmapDrawable) {
            setImageBitmap(it.bitmap)
        }
    }
}