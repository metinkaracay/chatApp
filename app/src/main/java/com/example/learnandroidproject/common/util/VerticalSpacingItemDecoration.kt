package com.example.learnandroidproject.common.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalSpacingItemDecoration(private val space: Int, private val excludeFirstItem: Boolean = false) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //outRect.left = space
        //outRect.right = space
        outRect.bottom = space

        if (parent.getChildAdapterPosition(view) == 0 && !excludeFirstItem) {
            outRect.top = space
        }
    }
}