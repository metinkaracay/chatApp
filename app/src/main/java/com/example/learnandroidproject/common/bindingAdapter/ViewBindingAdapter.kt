package com.example.learnandroidproject.common.bindingAdapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.DimenRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import com.example.learnandroidproject.common.extensions.dp

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("layoutMargin")
    fun setLayoutMarginWithDimenRes(view: View, @DimenRes resource: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        val margin = view.context.resources.getDimensionPixelSize(resource)
        layoutParams.setMargins(margin, margin, margin, margin)
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter("android:layout_marginBottom")
    fun setLayoutMarginBottom(view: View, marginBottom: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 0, 0, marginBottom)
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter("android:layout_marginEnd")
    fun setLayoutMarginEnd(view: View, marginEnd: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 0, marginEnd, 0)
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter("android:layout_margin")
    fun setLayoutMargin(view: View, margin: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(margin, margin, margin, margin)
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter("layoutGravity")
    fun setLayoutGravity(view: View, start: Boolean) {
        if (view.layoutParams is LinearLayoutCompat.LayoutParams) {
            (view.layoutParams as LinearLayoutCompat.LayoutParams).gravity = if (start) Gravity.START else Gravity.END
        } else if (view.layoutParams is FrameLayout.LayoutParams) {
            (view.layoutParams as FrameLayout.LayoutParams).gravity = if (start) Gravity.START else Gravity.END
        }
    }

    @JvmStatic
    @BindingAdapter("layoutAlign")
    fun setLayoutAlign(view: View, align: Int) {
        view.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply { addRule(align) }
    }
}