package com.example.learnandroidproject.common.extensions

import com.zyyoona7.wheel.WheelView

fun <T> WheelView<T>.onWheelSelected(func: (Int) -> Unit) {
    this.onWheelChangedListener = object : WheelView.OnWheelChangedListener {
        override fun onWheelScroll(scrollOffsetY: Int) {
        }

        override fun onWheelItemChanged(oldPosition: Int, newPosition: Int) {
        }

        override fun onWheelSelected(position: Int) {
            func.invoke(position)
        }

        override fun onWheelScrollStateChanged(state: Int) {
        }
    }
}