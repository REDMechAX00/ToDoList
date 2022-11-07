package com.redmechax00.todolist.recycler_view.views.main

import com.redmechax00.todolist.recycler_view.views.ItemView

data class ViewNew(
    override val itemId: String,
    override var itemPosition: Int,
    var itemLevel: String,
    override var itemText: String
) : ItemView {

    override fun getTypeView(): Int {
        return ItemView.ITEM_NEW
    }

    override fun copy(): ItemView {
        return ViewNew(
            itemId,
            itemPosition,
            itemLevel,
            itemText
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewNew

        if (itemId != other.itemId) return false
        if (itemPosition != other.itemPosition) return false
        if (itemLevel != other.itemLevel) return false
        if (itemText != other.itemText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + itemPosition
        result = 31 * result + itemLevel.hashCode()
        result = 31 * result + itemText.hashCode()
        return result
    }

}