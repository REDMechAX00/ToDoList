package com.redmechax00.todolist.recycler_view.holders.tasks

import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.R
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDeadline
import com.redmechax00.todolist.utils.ITEM_TIME_VALUE_NONE
import com.redmechax00.todolist.utils.getDateFromTime

class HolderDeadlineItem(view: View) : RecyclerView.ViewHolder(view), ItemHolder,
    View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var view: ViewDeadline
    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var onCheckedChangeListener: OnCheckedChangeListener
    private val tasksSwitchBox: SwitchCompat = view.findViewById(R.id.tasks_deadline_item_switch)
    private val tasksDeadlineText: TextView = view.findViewById(R.id.tasks_deadline_item_text)

    interface OnCheckedChangeListener {
        fun onSwitchBoxChange(isChecked: Boolean)
    }

    override fun drawItem(view: ItemView) {
        view as ViewDeadline
        this.view = view

        updateDeadlineText()
    }

    private fun updateDeadlineText() {
        tasksSwitchBox.setOnCheckedChangeListener(null)
        val time = view.itemDeadline
        if (time != ITEM_TIME_VALUE_NONE) {
            val date = getDateFromTime(time)
            tasksDeadlineText.visibility = View.VISIBLE
            tasksDeadlineText.text = date
            tasksSwitchBox.isChecked = true
        } else {
            tasksDeadlineText.visibility = View.GONE
            tasksSwitchBox.isChecked = false
        }
        tasksSwitchBox.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        onCheckedChangeListener.onSwitchBoxChange(isChecked)
    }

    override fun onClick(v: View?) {
        onItemClickListener.onItemClick(this, view)
    }

    override fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
        itemView.setOnClickListener(this)
    }

    fun setOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener
        tasksSwitchBox.setOnCheckedChangeListener(this)
    }

    override fun onDetach() {
        tasksSwitchBox.setOnCheckedChangeListener(null)
        itemView.setOnClickListener(null)
    }

}