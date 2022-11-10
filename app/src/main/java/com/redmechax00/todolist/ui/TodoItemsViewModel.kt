package com.redmechax00.todolist.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.redmechax00.todolist.data.ItemDao
import com.redmechax00.todolist.data.ItemDatabase
import com.redmechax00.todolist.data.TodoItemsRepository
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.recycler_view.views.main.ViewNew
import com.redmechax00.todolist.utils.*

class TodoItemsViewModel : ViewModel(), Contract.Model {

    private val repository = TodoItemsRepository()
    private var itemDao: ItemDao
    private var mSettings: SharedPreferences? = null
    val visibleDataLiveMutable: MutableLiveData<MutableList<ItemView>> = MutableLiveData()
    val editedItemLiveMutable: MutableLiveData<ViewBase?> = MutableLiveData()
    private var hiddenData: MutableList<ItemView> = mutableListOf()
    private var currentFragment: Fragment? = null
    private var successItemsIsHidden: Boolean = true

    init {
        itemDao = initDatabase()

        mSettings = initSettings()
        successItemsIsHidden = mSettings!!.getBoolean(APP_PREFERENCES_VISIBILITY, true)

        repository.getItems(itemDao) {
            splitData(it) { visibleData, hiddenData ->
                this.hiddenData = hiddenData
                visibleDataLiveMutable.value = updateItemsBackground(visibleData)
            }
        }
    }

    private fun initDatabase(): ItemDao {
        val db: ItemDatabase = Room.databaseBuilder(
            APP_ACTIVITY,
            ItemDatabase::class.java, TODO_ITEMS_DATABASE
        ).build()
        return db.getItemDao()
    }

    private fun initSettings(): SharedPreferences =
        APP_ACTIVITY.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun saveData(
        newDataVisible: MutableList<ItemView>,
        newDataHidden: MutableList<ItemView>
    ) {
        repository.saveItems(itemDao, connectData(newDataVisible, newDataHidden))

        setVisibleNewData(newDataVisible)
        setHiddenNewData(newDataHidden)
    }

    private fun setVisibleNewData(newDataVisible: MutableList<ItemView>) {
        val newData = updateItemsBackground(newDataVisible)
        visibleDataLiveMutable.value = newData
    }

    override fun getVisibleData(): MutableList<ItemView> {
        return deepCopy(visibleDataLiveMutable.value!!)
    }

    private fun setHiddenNewData(newDataHidden: MutableList<ItemView>) {
        hiddenData = newDataHidden
    }

    override fun getHiddenData(): MutableList<ItemView> {
        return hiddenData
    }

    override fun setCurrentFragment(fragment: Fragment) {
        currentFragment = fragment
    }

    override fun getCurrentFragment(): Fragment? {
        return currentFragment
    }

    override fun setEditedItem(editedItem: ViewBase?) {
        editedItemLiveMutable.value = editedItem
    }

    override fun getEditedItem(): ViewBase? {
        return editedItemLiveMutable.value
    }

    override fun setSuccessItemsIsHidden(successItemsIsHidden: Boolean) {
        this.successItemsIsHidden = successItemsIsHidden
        val editor: SharedPreferences.Editor = mSettings!!.edit()
        editor.putBoolean(APP_PREFERENCES_VISIBILITY, successItemsIsHidden)
        editor.apply()
    }

    override fun getSuccessItemsIsHidden(): Boolean {
        return successItemsIsHidden
    }

    private fun connectData(visibleData: MutableList<ItemView>, hiddenData: MutableList<ItemView>): MutableList<ItemView> {
        val connectedData: MutableList<ItemView> = deepCopy(visibleData)
        hiddenData.forEach { item ->
            connectedData.add(item)
        }
        connectedData.sortBy { it.itemPosition.inv() }
        return connectedData
    }

    private fun splitData(
        data: MutableList<ItemView>,
        onSuccess: (visibleData: MutableList<ItemView>, hiddenData: MutableList<ItemView>) -> Unit
    ) {
        var visibleData: MutableList<ItemView> = mutableListOf()
        val hiddenData: MutableList<ItemView> = mutableListOf()
        if (getSuccessItemsIsHidden()) {
            data.forEach {
                when (it.getTypeView()) {
                    ITEM_TYPE_BASE -> {
                        it as ViewBase
                        if (it.itemIsSuccess) {
                            hiddenData.add(it)
                        } else {
                            visibleData.add(it)
                        }
                    }
                    ITEM_TYPE_NEW -> {
                        visibleData.add(it)
                    }
                }
            }
        } else {
            visibleData = data
        }
        visibleData.sortBy { it.itemPosition.inv() }
        onSuccess(visibleData, hiddenData)
    }

    private fun updateItemsBackground(data: MutableList<ItemView>): MutableList<ItemView> {
        data.forEachIndexed { index, itemView ->
            when (itemView.getTypeView()) {
                ITEM_TYPE_BASE -> {
                    itemView as ViewBase
                    when (index) {
                        0 -> {
                            itemView.itemLevel = ITEM_LEVEL_TOP
                            if (data.size - 1 == 0) {
                                itemView.itemLevel = ITEM_LEVEL_LONELY
                            }
                        }
                        data.size - 1 -> itemView.itemLevel = ITEM_LEVEL_BOTTOM
                        else -> itemView.itemLevel = ITEM_LEVEL_MIDDLE
                    }
                }
                ITEM_TYPE_NEW -> {
                    itemView as ViewNew
                    when (index) {
                        0 -> {
                            itemView.itemLevel = ITEM_LEVEL_TOP
                            if (data.size - 1 == 0) {
                                itemView.itemLevel = ITEM_LEVEL_LONELY
                            }
                        }
                        data.size - 1 -> itemView.itemLevel = ITEM_LEVEL_BOTTOM
                        else -> itemView.itemLevel = ITEM_LEVEL_MIDDLE
                    }
                }
            }
        }

        return data
    }

    private fun deepCopy(data: MutableList<ItemView>): MutableList<ItemView> {
        return (data.map { it.copy() }).toMutableList()
    }
}