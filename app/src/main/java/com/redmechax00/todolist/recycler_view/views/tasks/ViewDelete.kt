package com.redmechax00.todolist.recycler_view.views.tasks

import com.redmechax00.todolist.recycler_view.views.ItemView

data class ViewDelete(
    override val itemId: String,
    override var itemPosition: Int,
    override var itemText: String
) : ItemView {

    override fun getTypeView(): Int {
        return ItemView.ITEM_DELETE
    }

    override fun copy(): ItemView {
        return ViewDelete(
            itemId,
            itemPosition,
            itemText
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewDelete

        if (itemId != other.itemId) return false
        if (itemText.isEmpty() != other.itemText.isEmpty()) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + itemPosition
        result = 31 * result + itemText.hashCode()
        return result
    }
}