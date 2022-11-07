package com.redmechax00.todolist.recycler_view

import com.redmechax00.todolist.App
import com.redmechax00.todolist.R
import com.redmechax00.todolist.data.TodoItemData
import com.redmechax00.todolist.recycler_view.views.ViewFactory
import com.redmechax00.todolist.utils.ITEM_LEVEL_LONELY
import com.redmechax00.todolist.utils.ITEM_POSITION_NEW
import com.redmechax00.todolist.utils.ITEM_TYPE_NEW
import com.redmechax00.todolist.utils.generateNewId

object MainObject {

    fun getItems() = mutableListOf(
        ViewFactory.getView(
            TodoItemData(
                ITEM_TYPE_NEW,
                ITEM_POSITION_NEW,
                ITEM_LEVEL_LONELY,
                generateNewId(ITEM_POSITION_NEW),
                App.Strings.get(R.string.text_item_new)
            )
        )
    )
}