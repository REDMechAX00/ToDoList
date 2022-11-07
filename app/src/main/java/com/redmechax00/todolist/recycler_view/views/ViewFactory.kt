package com.redmechax00.todolist.recycler_view.views

import com.redmechax00.todolist.data.TodoItemData
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.recycler_view.views.main.ViewNew
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDeadline
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDelete
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDesc
import com.redmechax00.todolist.recycler_view.views.tasks.ViewImportance
import com.redmechax00.todolist.utils.*

class ViewFactory {
    companion object {
        fun getView(item: TodoItemData): ItemView {
            return when (item.itemType) {
                ITEM_TYPE_NEW -> ViewNew(
                    item.itemId,
                    item.itemPosition,
                    item.itemLevel,
                    item.itemText
                )
                ITEM_TYPE_DESC -> ViewDesc(
                    item.itemId,
                    item.itemPosition,
                    item.itemText
                )
                ITEM_TYPE_IMPORTANCE -> ViewImportance(
                    item.itemId,
                    item.itemPosition,
                    item.itemText,
                    item.itemImportance
                )
                ITEM_TYPE_DEADLINE -> ViewDeadline(
                    item.itemId,
                    item.itemPosition,
                    item.itemText,
                    item.itemDeadline
                )
                ITEM_TYPE_DELETE -> ViewDelete(
                    item.itemId,
                    item.itemPosition,
                    item.itemText
                )
                else -> ViewBase(
                    item.itemId,
                    item.itemPosition,
                    item.itemLevel,
                    item.itemText,
                    item.itemImportance,
                    item.itemDeadline,
                    item.itemIsSuccess,
                    item.itemDateCreated,
                    item.itemDateChanged
                )
            }
        }
    }
}