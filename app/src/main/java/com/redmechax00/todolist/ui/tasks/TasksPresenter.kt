package com.redmechax00.todolist.ui.tasks

import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.ui.Contract
import com.redmechax00.todolist.ui.TodoItemsViewModel
import com.redmechax00.todolist.recycler_view.TasksObject
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.utils.*
import java.util.*

class TasksPresenter(
    tasksView: Contract.TasksView,
    tasksModel: TodoItemsViewModel
) : Contract.TasksPresenter {

    private var view: Contract.TasksView = tasksView
    private var model: TodoItemsViewModel = tasksModel

    init {
        view.initView()
        initData()

        model.editedItemLiveMutable.observe(view.getTasksLifecycleOwner()) { editedItem ->
            view.updateAdapter(TasksObject.getTasksItems(editedItem))
        }
    }

    override fun onResume() {
        model.setCurrentFragment(view.getTasksFragment())
    }

    override fun onClosePressed() {
        view.showCloseDialog()
    }

    override fun onSavePressed() {
        saveData()
        view.popBackStack()
    }

    override fun onTextChanged(s: CharSequence?) {
        val item = model.getEditedItem()
        item?.let {
            item.itemText = s.toString()
            model.setEditedItem(item)
        }
    }

    override fun onItemImportanceClick(holder: RecyclerView.ViewHolder) {
        view.showImportancePopupMenu(holder)
    }

    override fun onItemDeadlineClick() {
        view.showDeadlineDatePickerDialog(getCurrentCalendar())
    }

    override fun onSwitchBoxDeadlineChange(isChecked: Boolean) {
        if (isChecked) {
            view.showDeadlineDatePickerDialog(getCurrentCalendar())
        } else {
            val editedItem = model.getEditedItem()
            editedItem?.itemDeadline = -1
            model.setEditedItem(editedItem)
        }
    }

    override fun onItemDeleteClick() {
        view.showDeleteDialog()
    }

    override fun onImportancePopupMenuNoClick() {
        val item = model.getEditedItem()
        item?.let {
            item.itemImportance =
                ITEM_IMPORTANCE_NO
            model.setEditedItem(item)
        }
    }

    override fun onImportancePopupMenuLowClick() {
        val item = model.getEditedItem()
        item?.let {
            item.itemImportance =
                ITEM_IMPORTANCE_LOW
            model.setEditedItem(item)
        }
    }

    override fun onImportancePopupMenuHighClick() {
        val item = model.getEditedItem()
        item?.let {
            item.itemImportance =
                ITEM_IMPORTANCE_HIGH
            model.setEditedItem(item)
        }
    }

    override fun onDeadlineDateDialogPositiveClick(year: Int, month: Int, dayOfMonth: Int) {
        val time = getTime(year, month, dayOfMonth)
        time?.let {
            val editedItem = model.getEditedItem()
            editedItem?.itemDeadline = time
            model.setEditedItem(editedItem)
        }
    }

    override fun onDeadlineDateDialogNegativeClick() {
        val editedItem = model.getEditedItem()
        editedItem?.itemDeadline = -1
        model.setEditedItem(editedItem)
    }

    override fun onCloseDialogPositiveClick() {
        view.popBackStack()
    }

    override fun onDeleteDialogPositiveClick() {
        deleteEditedItem()
    }

    private fun deleteEditedItem(){
        val editedItemId = model.getEditedItem()?.itemId
        val visibleData = model.getVisibleData()

        val deletedItem = visibleData.find { it.itemId == editedItemId }
        if (deletedItem != null) {
            visibleData.remove(deletedItem)
            model.saveData(visibleData, model.getHiddenData())
        }

        view.popBackStack()
    }



    private fun initData() {
        var editedItem: ViewBase? = model.getEditedItem()
        if (editedItem == null) {
            val bundle = view.getTasksArguments()
            val itemId: String? = bundle?.getString(TAG_ITEM_ID)
            val item = model.getVisibleData().find { it.itemId == itemId }

            if (item != null && item is ViewBase) {
                editedItem = item
            }
        }

        editedItem = ViewBase(
            editedItem?.itemId ?: ITEM_ID_NEW,
            editedItem?.itemPosition ?: getNewPosition(),
            ITEM_LEVEL_TOP,
            editedItem?.itemText ?: "",
            editedItem?.itemImportance ?: ITEM_IMPORTANCE_NO,
            editedItem?.itemDeadline ?: -1,
            editedItem?.itemIsSuccess ?: false,
            editedItem?.itemDateCreated ?: -1,
            editedItem?.itemDateChanged ?: -1
        )

        model.setEditedItem(editedItem)
    }

    private fun saveData() {
        val editedItem = model.getEditedItem()
        editedItem?.itemId?.let {
            editedItem.itemId = when (editedItem.itemId) {
                ITEM_ID_NEW -> generateNewId(getNewPosition())
                else -> editedItem.itemId
            }
        }

        val visibleData = model.getVisibleData()

        editedItem?.let {
            val sameItemInData = visibleData.find { it.itemId == editedItem.itemId }

            val currentTime = getCurrentTime()
            editedItem.itemDateChanged = currentTime

            if (sameItemInData == null) {
                editedItem.itemDateCreated = currentTime
                visibleData.add(0, editedItem)
            } else {
                sameItemInData as ViewBase
                sameItemInData.itemText = editedItem.itemText
                sameItemInData.itemImportance = editedItem.itemImportance
                sameItemInData.itemDeadline = editedItem.itemDeadline
                sameItemInData.itemIsSuccess = editedItem.itemIsSuccess
                sameItemInData.itemDateCreated = editedItem.itemDateCreated
                sameItemInData.itemDateChanged = editedItem.itemDateChanged
            }

            model.saveData(visibleData, model.getHiddenData())
        }
    }

    private fun getCurrentCalendar(): Calendar {
        var itemDeadline = model.getEditedItem()?.itemDeadline
        if (itemDeadline == -1L) {
            itemDeadline = null
        }

        val c = Calendar.getInstance()
        c.time = Date(itemDeadline ?: c.timeInMillis)

        return c
    }

    private fun getNewPosition(): Int {
        val visibleData = model.getVisibleData()
        val hiddenData = model.getHiddenData()

        var maxPosition = 0

        visibleData.forEach {
            maxPosition = it.itemPosition.coerceAtLeast(maxPosition)
        }

        hiddenData.forEach {
            maxPosition = it.itemPosition.coerceAtLeast(maxPosition)
        }

        return maxPosition + 1
    }

    override fun onDestroy() {

    }
}