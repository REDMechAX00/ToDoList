package com.redmechax00.todolist.recycler_view.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.R
import com.redmechax00.todolist.recycler_view.holders.main.HolderBaseItem
import com.redmechax00.todolist.recycler_view.holders.main.HolderNewItem
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderDeadlineItem
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderDeleteItem
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderDescItem
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderImportanceItem

class HolderFactory {
    companion object {
        fun getHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType){
                ItemView.ITEM_NEW -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.main_new_item, parent, false)
                    HolderNewItem(view, parent.context)
                }
                ItemView.ITEM_DESC -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.tasks_desc_item, parent, false)
                    HolderDescItem(view)
                }
                ItemView.ITEM_IMPORTANCE -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.tasks_importance_item, parent, false)
                    HolderImportanceItem(view)
                }
                ItemView.ITEM_DEADLINE -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.tasks_deadline_item, parent, false)
                    HolderDeadlineItem(view)
                }
                ItemView.ITEM_DELETE -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.tasks_delete_item, parent, false)
                    HolderDeleteItem(view, parent.context)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.main_base_item, parent, false)
                    HolderBaseItem(view, parent.context)
                }
            }
        }
    }
}