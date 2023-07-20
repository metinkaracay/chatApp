package com.example.learnandroidproject.ui.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDialog<DB : ViewDataBinding>(activity: Activity) : Dialog(activity) {

    lateinit var binding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutResId(), null, false)
        setContentView(binding.root)
    }

    @LayoutRes
    protected abstract fun getLayoutResId(): Int
}