package com.redmechax00.todolist.recycler_view.views.main

import com.redmechax00.todolist.recycler_view.views.ItemView

data class ViewBase(
    override var itemId: String,
    override var itemPosition: Int,
    var itemLevel: String,
    override var itemText: String,
    var itemImportance: String,
    var itemDeadline: Long,
    var itemIsSuccess: Boolean,
    var itemDateCreated: Long,
    var itemDateChanged: Long
) : ItemView {

    override fun getTypeView(): Int {
        return ItemView.ITEM_BASE
    }

    override fun copy(): ItemView {
        return ViewBase(
            itemId,
            itemPosition,
            itemLevel,
            itemText,
            itemImportance,
            itemDeadline,
            itemIsSuccess,
            itemDateCreated,
            itemDateChanged
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewBase

        if (itemId != other.itemId) return false
        if (itemLevel != other.itemLevel) return false
        if (itemText != other.itemText) return false
        if (itemImportance != other.itemImportance) return false
        if (itemDeadline != other.itemDeadline) return false
        if (itemIsSuccess != other.itemIsSuccess) return false
        if (itemDateCreated != other.itemDateCreated) return false
        if (itemDateChanged != other.itemDateChanged) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + itemPosition
        result = 31 * result + itemLevel.hashCode()
        result = 31 * result + itemText.hashCode()
        result = 31 * result + itemImportance.hashCode()
        result = 31 * result + itemDeadline.hashCode()
        result = 31 * result + itemIsSuccess.hashCode()
        result = 31 * result + itemDateCreated.hashCode()
        result = 31 * result + itemDateChanged.hashCode()
        return result
    }

}