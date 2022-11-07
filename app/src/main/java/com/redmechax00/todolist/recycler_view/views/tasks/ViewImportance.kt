package com.redmechax00.todolist.recycler_view.views.tasks

import com.redmechax00.todolist.recycler_view.views.ItemView

data class ViewImportance(
    override val itemId: String,
    override var itemPosition: Int,
    override var itemText: String,
    var itemImportance: String
) : ItemView {

    override fun getTypeView(): Int {
        return ItemView.ITEM_IMPORTANCE
    }

    override fun copy(): ItemView {
        return ViewImportance(
            itemId,
            itemPosition,
            itemText,
            itemImportance
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewImportance

        if (itemId != other.itemId) return false
        if (itemImportance != other.itemImportance) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + itemPosition
        result = 31 * result + itemText.hashCode()
        result = 31 * result + itemImportance.hashCode()
        return result
    }

}