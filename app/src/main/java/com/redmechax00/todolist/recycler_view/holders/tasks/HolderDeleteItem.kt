package com.redmechax00.todolist.recycler_view.holders.tasks

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.App
import com.redmechax00.todolist.R
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDelete
import com.redmechax00.todolist.utils.ITEM_ID_NEW

class HolderDeleteItem(view: View, private val context: Context) : RecyclerView.ViewHolder(view), ItemHolder, View.OnClickListener {

    private lateinit var view: ViewDelete
    private lateinit var onItemClickListener: OnItemClickListener
    private val deleteText: TextView = view.findViewById(R.id.tasks_delete_item_text)
    private val deleteImage: ImageView = view.findViewById(R.id.tasks_delete_item_image)

    override fun drawItem(view: ItemView) {
        view as ViewDelete
        this.view = view

        updateActivatedStatus(view.itemText)
    }

    private fun updateActivatedStatus(text: String){
        if(text.isEmpty() && view.itemId == ITEM_ID_NEW){
            deleteImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tasks_delete_disabled))
            deleteText.setTextColor(App.Colors.get(R.color.color_label_disable))
            itemView.isEnabled = false
        } else {
            deleteImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tasks_delete_enabled))
            deleteText.setTextColor(App.Colors.get(R.color.color_action_delete))
            itemView.isEnabled = true
        }
    }

    override fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onItemClickListener.onItemClick(this ,view)
    }

    override fun onDetach() {
        itemView.setOnClickListener(null)
    }

}