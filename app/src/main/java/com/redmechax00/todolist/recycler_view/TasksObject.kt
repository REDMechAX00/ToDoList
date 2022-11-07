package com.redmechax00.todolist.recycler_view

import com.redmechax00.todolist.data.TodoItemData
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.ViewFactory
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.utils.*

object TasksObject {

    fun getTasksItems(editedItem: ViewBase?): MutableList<ItemView> {

        val itemId = editedItem?.itemId ?: ITEM_ID_NEW
        val itemText = editedItem?.itemText ?: ""
        val itemImportance = editedItem?.itemImportance ?: ITEM_IMPORTANCE_NO
        val itemDeadline = editedItem?.itemDeadline ?: ITEM_TIME_VALUE_NONE

        return mutableListOf(
            ViewFactory.getView(
                TodoItemData(
                    ITEM_TYPE_DESC,
                    3,
                    ITEM_LEVEL_MIDDLE,
                    itemId,
                    itemText
                )
            ),
            ViewFactory.getView(
                TodoItemData(
                    ITEM_TYPE_IMPORTANCE,
                    2,
                    ITEM_LEVEL_MIDDLE,
                    itemId,
                    itemText,
                    itemImportance
                )
            ),
            ViewFactory.getView(
                TodoItemData(
                    ITEM_TYPE_DEADLINE,
                    1,
                    ITEM_LEVEL_MIDDLE,
                    itemId,
                    itemText,
                    itemImportance,
                    itemDeadline
                )
            ),
            ViewFactory.getView(
                TodoItemData(
                    ITEM_TYPE_DELETE,
                    0,
                    ITEM_LEVEL_MIDDLE,
                    itemId,
                    itemText
                )
            )
        )
    }
}