package com.redmechax00.todolist.recycler_view.holders.main

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.R
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.main.ViewNew
import com.redmechax00.todolist.utils.ITEM_LEVEL_BOTTOM
import com.redmechax00.todolist.utils.ITEM_LEVEL_MIDDLE
import com.redmechax00.todolist.utils.ITEM_LEVEL_TOP

class HolderNewItem(view: View, private val context: Context) : RecyclerView.ViewHolder(view), ItemHolder, View.OnClickListener {

    private lateinit var view: ViewNew
    private val itemText: TextView = view.findViewById(R.id.main_new_item_text)

    private lateinit var onItemClickListener: OnItemClickListener

    override fun drawItem(view: ItemView) {
        (view as ViewNew)
        this.view = view

        itemText.text = view.itemText

        setItemBackground()
    }

    override fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onItemClickListener.onItemClick(this ,view)
    }

    private fun setItemBackground() {
        when (view.itemLevel) {
            ITEM_LEVEL_TOP -> itemView.background =
                ContextCompat.getDrawable(context, R.drawable.item_top_bg)
            ITEM_LEVEL_MIDDLE -> itemView.background =
                ContextCompat.getDrawable(context, R.drawable.item_middle_bg)
            ITEM_LEVEL_BOTTOM -> itemView.background =
                ContextCompat.getDrawable(context, R.drawable.item_bottom_bg)
            else -> itemView.background =
                ContextCompat.getDrawable(context, R.drawable.item_lonely_bg)
        }
    }

    override fun onDetach() {
        itemView.setOnClickListener(null)
    }
}