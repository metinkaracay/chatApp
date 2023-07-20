package com.example.learnandroidproject.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.learnandroidproject.R

abstract class BaseDialogFragment<DB : ViewDataBinding> : DialogFragment() {

    lateinit var binding: DB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, getLayoutResId(), container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.DialogOpeningAnimation)
        return super.onCreateDialog(savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getLayoutResId(): Int
}