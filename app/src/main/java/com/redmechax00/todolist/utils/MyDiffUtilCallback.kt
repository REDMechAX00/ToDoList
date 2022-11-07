package com.redmechax00.todolist.utils

import androidx.recyclerview.widget.DiffUtil
import com.redmechax00.todolist.recycler_view.views.ItemView

class MyDiffUtilCallback : DiffUtil.ItemCallback<ItemView>() {

    override fun areItemsTheSame(oldItem: ItemView, newItem: ItemView): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ItemView, newItem: ItemView): Boolean {
        return oldItem == newItem
    }

}