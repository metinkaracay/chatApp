package com.example.learnandroidproject.common.bindingAdapter

import androidx.databinding.BindingAdapter
import com.devlomi.record_view.RecordView

object RecordViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("slideToCancelText")
    fun setSlideToCancelText(recordView: RecordView, text: String) {
        recordView.setSlideToCancelText(text)
    }
}