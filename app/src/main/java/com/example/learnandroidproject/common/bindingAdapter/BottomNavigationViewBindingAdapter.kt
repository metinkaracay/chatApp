package com.example.learnandroidproject.common.bindingAdapter

import androidx.annotation.MenuRes
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavigationViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("dynamicMenu")
    fun dynamicMenu(bottomNavigationView: BottomNavigationView, @MenuRes resource: Int?) {
        if (resource != null && resource != 0 && bottomNavigationView.menu.size() == 0) {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(resource)
        }
    }
}