package com.redmechax00.todolist.recycler_view

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.redmechax00.todolist.recycler_view.views.ItemView

interface OnItemClickListener{
    fun onItemClick(holder: ViewHolder, itemView: ItemView)
}