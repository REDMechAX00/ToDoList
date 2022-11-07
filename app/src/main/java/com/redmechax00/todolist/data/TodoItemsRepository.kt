package com.redmechax00.todolist.data

import com.redmechax00.todolist.recycler_view.MainObject
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.TodoItemFactory
import com.redmechax00.todolist.recycler_view.views.ViewFactory
import kotlinx.coroutines.*

class TodoItemsRepository {

    @OptIn(DelicateCoroutinesApi::class)
    fun getItems(itemDao: ItemDao, onSuccess:(data:MutableList<ItemView>)-> Unit){
        val result: Deferred<List<TodoItemData>> = GlobalScope.async {
            return@async itemDao.getAllItems()
        }

        GlobalScope.launch(Dispatchers.Main) {
            if (result.await().isNotEmpty()) {
                val data = mutableListOf<ItemView>()
                result.await().forEach {
                    data.add(ViewFactory.getView(it))
                }
                onSuccess(data)
            } else {
                onSuccess(MainObject.getItems())
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveItems(itemDao: ItemDao, newData: MutableList<ItemView>) {
        GlobalScope.launch(Dispatchers.IO) {
            itemDao.deleteAll()
        }
        val convertedData = convertItemViewToItemData(newData)
        GlobalScope.launch(Dispatchers.IO) {
            convertedData.forEachIndexed { _, item ->
                itemDao.add(item)
            }
        }
    }

    private fun convertItemViewToItemData(itemViewData: MutableList<ItemView>): ArrayList<TodoItemData> {
        val itemData = arrayListOf<TodoItemData>()
        itemViewData.forEach {
            itemData.add(TodoItemFactory.getTodoItem(it))
        }
        return itemData
    }

}