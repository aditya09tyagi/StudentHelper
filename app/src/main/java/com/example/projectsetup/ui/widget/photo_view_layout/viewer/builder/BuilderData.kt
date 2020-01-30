package com.example.projectsetup.ui.widget.photo_view_layout.viewer.builder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.example.projectsetup.ui.widget.photo_view_layout.listeners.OnDismissListener
import com.example.projectsetup.ui.widget.photo_view_layout.listeners.OnImageChangeListener
import com.example.projectsetup.ui.widget.photo_view_layout.loader.ImageLoader

internal class BuilderData<T>(
    val images: List<T>,
    val imageLoader: ImageLoader<T>
) {
    var backgroundColor = Color.BLACK
    var startPosition: Int = 0
    var imageChangeListener: OnImageChangeListener? = null
    var onDismissListener: OnDismissListener? = null
    var overlayView: View? = null
    var imageMarginPixels: Int = 0
    var containerPaddingPixels = IntArray(4)
    var shouldStatusBarHide = true
    var isZoomingAllowed = true
    var isSwipeToDismissAllowed = true
    var transitionView: ImageView? = null
}