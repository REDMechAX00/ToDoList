package com.redmechax00.todolist.recycler_view.holders

import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.views.ItemView

interface ItemHolder {
    fun drawItem(view: ItemView)
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener)
    fun onDetach()
}