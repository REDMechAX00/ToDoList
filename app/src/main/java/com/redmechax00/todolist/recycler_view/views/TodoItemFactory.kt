package com.redmechax00.todolist.recycler_view.views

import com.redmechax00.todolist.data.TodoItemData
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.recycler_view.views.main.ViewNew
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDeadline
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDelete
import com.redmechax00.todolist.recycler_view.views.tasks.ViewDesc
import com.redmechax00.todolist.recycler_view.views.tasks.ViewImportance
import com.redmechax00.todolist.utils.*

class TodoItemFactory {
    companion object {
        fun getTodoItem(item: ItemView): TodoItemData {
            return when (item.getTypeView()) {
                ITEM_TYPE_NEW -> {
                    item as ViewNew
                    TodoItemData(
                        ITEM_TYPE_NEW,
                        item.itemPosition,
                        item.itemLevel,
                        item.itemId,
                        item.itemText
                    )
                }
                else -> {
                    item as ViewBase
                    TodoItemData(
                        ITEM_TYPE_BASE,
                        item.itemPosition,
                        item.itemLevel,
                        item.itemId,
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
}