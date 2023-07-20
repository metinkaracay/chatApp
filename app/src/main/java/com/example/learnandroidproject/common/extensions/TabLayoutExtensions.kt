package com.example.learnandroidproject.common.extensions

import com.google.android.material.tabs.TabLayout

fun TabLayout.afterTabChanged(afterTabChanged: (TabLayout.Tab) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            afterTabChanged.invoke(tab)
        }
    })
}