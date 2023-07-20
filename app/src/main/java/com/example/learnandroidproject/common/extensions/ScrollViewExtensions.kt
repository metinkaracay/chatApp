package com.example.learnandroidproject.common.extensions

import android.view.View
import android.widget.ScrollView
import kotlin.math.abs

fun ScrollView.getContentHeight(): Int {
    var scrollRange = 0
    if (this.childCount > 0) {
        val child: View = this.getChildAt(0)
        scrollRange = abs(child.height - (this.height - this.paddingBottom - this.paddingTop))
    }
    return scrollRange
}