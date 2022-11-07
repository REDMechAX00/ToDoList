package com.redmechax00.todolist.recycler_view.holders.tasks

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.R
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.views.ItemView

class HolderDescItem(view: View) : RecyclerView.ViewHolder(view), ItemHolder, TextWatcher{

    interface TextChangeListener {
        fun onTextChanged(s: CharSequence?)
    }

    private var descEdText: EditText = view.findViewById(R.id.tasks_desc_item_edittext)
    private lateinit var mTextChangeListener: TextChangeListener

    override fun drawItem(view: ItemView) {
        descEdText.setText(view.itemText)
    }

    override fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {}

    fun addTextChangeListener(textChangeListener: TextChangeListener){
        this.mTextChangeListener = textChangeListener
        descEdText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mTextChangeListener.onTextChanged(s)
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onDetach() {
        descEdText.addTextChangedListener(null)
    }

}