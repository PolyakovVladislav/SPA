package com.superpow.skill.ui.extensions

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewTreeObserver


fun View.handleFirstGlobalLayout(action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                action.invoke()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    )
}