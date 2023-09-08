package com.hirno.weather.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hirno.weather.R

class ItemOffsetDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(rect: Rect, v: View, parent: RecyclerView, s: RecyclerView.State) {
        val offset = v.context.resources.getDimensionPixelOffset(R.dimen.list_item_offset)
        rect.set(offset, offset, offset, offset)
    }
}