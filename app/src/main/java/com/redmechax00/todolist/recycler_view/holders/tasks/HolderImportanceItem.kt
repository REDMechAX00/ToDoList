package com.redmechax00.todolist.recycler_view.holders.tasks

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.App
import com.redmechax00.todolist.R
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.tasks.ViewImportance
import com.redmechax00.todolist.utils.ITEM_IMPORTANCE_HIGH
import com.redmechax00.todolist.utils.ITEM_IMPORTANCE_LOW


class HolderImportanceItem(view: View) : RecyclerView.ViewHolder(view), ItemHolder,
    View.OnClickListener {

    private lateinit var view: ViewImportance
    private lateinit var onItemClickListener: OnItemClickListener
    private val tasksImportanceItemText: TextView =
        view.findViewById(R.id.tasks_importance_item_text)

    override fun drawItem(view: ItemView) {
        view as ViewImportance
        this.view = view

        updateItemTextStyle(view)
    }

    private fun updateItemTextStyle(view: ViewImportance) {
        val itemText = when (view.itemImportance) {
            ITEM_IMPORTANCE_LOW -> App.Strings.get(R.string.text_item_importance_low)
            ITEM_IMPORTANCE_HIGH -> App.Strings.get(R.string.text_item_importance_high)
            else -> App.Strings.get(R.string.text_item_importance_no)
        }

        val itemTextColor = when (view.itemImportance) {
            ITEM_IMPORTANCE_LOW -> App.Colors.get(R.color.color_importance_low)
            ITEM_IMPORTANCE_HIGH -> App.Colors.get(R.color.color_importance_high)
            else -> App.Colors.get(R.color.color_importance_no)
        }

        tasksImportanceItemText.text = itemText
        tasksImportanceItemText.setTextColor(itemTextColor)
    }

    override fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onItemClickListener.onItemClick(this, view)
    }

    override fun onDetach() {
        itemView.setOnClickListener(null)
    }

}