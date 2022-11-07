package com.redmechax00.todolist.recycler_view.views.tasks

import android.util.Log
import com.redmechax00.todolist.recycler_view.views.ItemView

data class ViewDeadline(
    override val itemId: String,
    override var itemPosition: Int,
    override var itemText: String,
    var itemDeadline: Long
) : ItemView {

    override fun getTypeView(): Int {
        return ItemView.ITEM_DEADLINE
    }

    override fun copy(): ItemView {
        return ViewDeadline(
            itemId,
            itemPosition,
            itemText,
            itemDeadline
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewDeadline

        if (itemId != other.itemId) return false
        if (itemDeadline != other.itemDeadline || other.itemDeadline == -1L) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + itemPosition
        result = 31 * result + itemText.hashCode()
        result = 31 * result + itemDeadline.hashCode()
        return result
    }

}