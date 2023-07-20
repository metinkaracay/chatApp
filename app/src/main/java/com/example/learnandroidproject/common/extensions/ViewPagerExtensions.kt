package com.example.learnandroidproject.common.extensions

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.onPageChanged(func: (Int) -> Unit) {
    this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            func.invoke(position)
        }
    })
}

fun ViewPager.onPageChanged(func: (Int) -> Unit) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            func.invoke(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    })
}