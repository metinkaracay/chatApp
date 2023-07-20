package com.example.learnandroidproject.common.bindingAdapter

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object SwipeRefreshLayoutBindingAdapter {

    @JvmStatic
    @BindingAdapter("refreshing")
    fun setRefreshing(refreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
        refreshLayout.isRefreshing = refreshing
    }

    @JvmStatic
    @BindingAdapter("refreshEnabled")
    fun setRefreshEnabled(refreshLayout: SwipeRefreshLayout, enabled: Boolean) {
        refreshLayout.isEnabled = enabled
    }
}