package com.redmechax00.todolist.recycler_view.holders.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.App
import com.redmechax00.todolist.R
import com.redmechax00.todolist.databinding.TasksFragmentBinding
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.utils.*


class HolderBaseItem(view: View, private val context: Context) : RecyclerView.ViewHolder(view),
    ItemHolder, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var view: ViewBase
    private val baseCheckBox: CheckBox = view.findViewById(R.id.main_base_item_check)
    private val baseText: TextView = view.findViewById(R.id.main_base_item_text)
    private val baseTextImportance: TextView =
        view.findViewById(R.id.main_base_item_text_importance)
    private val baseTextDeadline: TextView = view.findViewById(R.id.main_base_item_text_deadline)

    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var onCheckedChangeListener: OnCheckedChangeListener

    interface OnCheckedChangeListener {
        fun onCheckBoxChange(position: Int, itemIsSuccess: Boolean)
    }

    override fun drawItem(view: ItemView) {
        (view as ViewBase)
        this.view = view

        baseText.text = view.itemText

        updateImportance()
        updateDeadlineText()
        changeItemIsSuccessViewState()
        setItemBackground()
    }

    override fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
        itemView.setOnClickListener(this)
    }

    fun setOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener
        baseCheckBox.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View?) {
        onItemClickListener.onItemClick(this, view)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        onCheckedChangeListener.onCheckBoxChange(absoluteAdapterPosition, isChecked)
    }

    fun getCheckBoxState(): Boolean {
        return baseCheckBox.isChecked
    }

    private fun updateImportance() {
        when (view.itemImportance) {
            ITEM_IMPORTANCE_NO -> {
                val uncheckedColor = App.Colors.get(R.color.color_control_normal)
                baseCheckBox.buttonTintList = getCheckBoxTint(uncheckedColor)
                baseTextImportance.visibility = View.GONE
            }
            ITEM_IMPORTANCE_LOW -> {
                val uncheckedColor = App.Colors.get(R.color.color_control_normal)
                baseCheckBox.buttonTintList = getCheckBoxTint(uncheckedColor)
                baseTextImportance.visibility = View.VISIBLE
                baseTextImportance.setTextAppearance(R.style.importanceLowText)
                baseTextImportance.text = App.Strings.get(R.string.text_item_importance_low_symbol)
            }
            ITEM_IMPORTANCE_HIGH -> {
                val uncheckedColor = App.Colors.get(R.color.color_control_increased)
                baseCheckBox.buttonTintList = getCheckBoxTint(uncheckedColor)
                baseTextImportance.visibility = View.VISIBLE
                baseTextImportance.setTextAppearance(R.style.importanceHighText)
                baseTextImportance.text = App.Strings.get(R.string.text_item_importance_high_symbol)
            }
        }
    }

    private fun updateDeadlineText() {
        val time = view.itemDeadline
        if (time != -1L) {
            val date = getDateFromTime(time)
            baseTextDeadline.visibility = View.VISIBLE
            baseTextDeadline.text = date
        } else {
            baseTextDeadline.visibility = View.GONE
        }
    }

    private fun changeItemIsSuccessViewState() {
        baseCheckBox.setOnCheckedChangeListener(null)
        baseCheckBox.isChecked = view.itemIsSuccess
        baseCheckBox.setOnCheckedChangeListener(this)

        if (view.itemIsSuccess) {
            baseText.paintFlags = baseText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            baseText.setTextAppearance(R.style.tertiaryText)
            baseTextImportance.setTextAppearance(R.style.importanceDisableText)
            baseTextDeadline.setTextAppearance(R.style.dateDisableText)
        } else {
            baseText.paintFlags =
                baseText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            baseText.setTextAppearance(R.style.primaryText)
            baseTextDeadline.setTextAppearance(R.style.dateNormalText)
        }
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

    private fun getCheckBoxTint(uncheckedColor: Int): ColorStateList {
        val checkedColor = App.Colors.get(R.color.color_control_activated)
        return ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                uncheckedColor,
                checkedColor
            )
        )
    }

    override fun onDetach() {
        itemView.setOnClickListener(null)
        baseCheckBox.setOnCheckedChangeListener(null)
    }
}