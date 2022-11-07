package com.redmechax00.todolist.recycler_view.views

import com.redmechax00.todolist.utils.*

interface ItemView {

    val itemId: String
    var itemPosition: Int
    var itemText: String

    companion object {
        val ITEM_BASE: Int
            get() = ITEM_TYPE_BASE
        val ITEM_NEW: Int
            get() = ITEM_TYPE_NEW
        val ITEM_DESC: Int
            get() = ITEM_TYPE_DESC
        val ITEM_IMPORTANCE: Int
            get() = ITEM_TYPE_IMPORTANCE
        val ITEM_DEADLINE: Int
            get() = ITEM_TYPE_DEADLINE
        val ITEM_DELETE: Int
            get() = ITEM_TYPE_DELETE
    }

    fun getTypeView(): Int

    fun copy(): ItemView

    override fun equals(other: Any?): Boolean
}