package com.redmechax00.todolist.ui.main

import android.os.CountDownTimer
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.ui.Contract
import com.redmechax00.todolist.ui.TodoItemsViewModel
import com.redmechax00.todolist.ui.tasks.TasksFragment
import com.redmechax00.todolist.utils.ITEM_ID_NEW
import com.redmechax00.todolist.utils.ITEM_TYPE_BASE
import com.redmechax00.todolist.utils.ITEM_TYPE_NEW
import com.redmechax00.todolist.utils.replaceFragment
import java.util.*
import kotlin.math.abs


class MainPresenter(
    mainView: Contract.MainView,
    mainModel: TodoItemsViewModel
) : Contract.MainPresenter {

    private var view: Contract.MainView = mainView
    private var model: TodoItemsViewModel = mainModel

    private var timer: CountDownTimer? = null
    private var deletedItem: ItemView? = null

    init {
        view.initView()

        model.visibleDataLiveMutable.observe(view.getMainLifecycleOwner()) { visibleData ->
            onUpdateVisibleData(visibleData)
        }
    }

    override fun onResume() {
        model.setCurrentFragment(view.getMainFragment())
        model.setEditedItem(null)

        onResumeBtnVisibility()
    }

    private fun onUpdateVisibleData(visibleData: MutableList<ItemView>) {
        view.updateAdapter(visibleData)
        view.updateTextSuccessInfo(getCountItemsIsSuccess())
    }

    override fun onResumeBtnVisibility() {
        if (model.getSuccessItemsIsHidden()) {
            view.setBtnVisibilityHidden()
        } else {
            view.setBtnVisibilityShown()
        }
    }

    override fun onOffsetChange(appBarLayout: AppBarLayout, verticalOffset: Int) {

        val alphaForToolbarItems = abs(verticalOffset) * (1f / appBarLayout.totalScrollRange)
        val alphaForCollapsingItems =
            1f - abs(verticalOffset) * (1f / appBarLayout.totalScrollRange) * 2f
        var visibilityForToolbarItems: Int = View.VISIBLE
        var visibilityForCollapsingItems: Int = View.VISIBLE

        when (abs(verticalOffset)) {
            0 -> {
                visibilityForToolbarItems = View.GONE
            }
            appBarLayout.totalScrollRange -> {
                visibilityForCollapsingItems = View.GONE
            }
            else -> {
                visibilityForToolbarItems = View.VISIBLE
                visibilityForCollapsingItems = View.VISIBLE
            }
        }

        view.setAlphaForCollapsingToolbarItems(alphaForToolbarItems, alphaForCollapsingItems)
        view.setVisibilityForCollapsingToolbarItems(
            visibilityForToolbarItems,
            visibilityForCollapsingItems
        )
    }

    override fun onBtnVisibilityClick() {
        setVisibilityMode()
    }

    override fun onFabNewClick() {
        startTaskFragment(ITEM_ID_NEW)
    }

    override fun onFabRestoreClick() {
        restoreItem()
    }

    override fun onItemClick(holder: RecyclerView.ViewHolder, itemView: ItemView) {
        startTaskFragment(itemView.itemId)
    }

    override fun onCheckBoxChange(position: Int, itemIsSuccess: Boolean) {
        setItemIsSuccess(position, itemIsSuccess)
    }

    override fun onDrag(fromIndex: Int, toIndex: Int) {
        moveItem(fromIndex, toIndex)
    }

    override fun onSwiped() {
        startLockSwipeTimer()
    }

    override fun onActionDelete(position: Int) {
        deleteItem(position)
    }

    override fun onActionCheck(position: Int) {
        setItemIsSuccess(position, true)
    }

    override fun onActionEnd() {
        model.saveData(model.getVisibleData(), model.getHiddenData())
    }

    private fun setVisibilityMode() {
        val hideSuccessItems = model.getSuccessItemsIsHidden()
        val visibleData = model.getVisibleData()
        val hiddenData = model.getHiddenData()

        if (hideSuccessItems) {
            //View
            // ---> Mode - Shown
            view.setBtnVisibilityAnimateShown()

            //Model
            hiddenData.forEach { item ->
                visibleData.add(item)
            }
            visibleData.sortBy { it.itemPosition.inv() }
            hiddenData.clear()

            model.setSuccessItemsIsHidden(false)
            model.saveData(visibleData, hiddenData)

        } else {
            //View
            // ---> Mode - Hidden
            view.setBtnVisibilityAnimateHidden()

            //Model
            val newVisibleData = mutableListOf<ItemView>()
            visibleData.forEach { item ->
                when (item.getTypeView()) {
                    ITEM_TYPE_BASE -> {
                        item as ViewBase
                        if (item.itemIsSuccess) {
                            hiddenData.add(item)
                        } else {
                            newVisibleData.add(item)
                        }
                    }
                    ITEM_TYPE_NEW -> newVisibleData.add(item)
                }
            }

            model.setSuccessItemsIsHidden(true)
            model.saveData(newVisibleData, hiddenData)
        }
    }

    private fun setItemIsSuccess(position: Int, itemIsSuccess: Boolean) {
        val visibleData: MutableList<ItemView> = model.getVisibleData()
        val hiddenData: MutableList<ItemView> = model.getHiddenData()
        val itemsIsHidden = model.getSuccessItemsIsHidden()

        if (position < visibleData.size && position != -1) {
            val item = (visibleData[position])
            when (item.getTypeView()) {
                ITEM_TYPE_BASE -> {
                    item as ViewBase
                    item.itemIsSuccess = itemIsSuccess

                    if (itemsIsHidden) {
                        if (itemIsSuccess) {
                            hiddenData.add(item)
                            visibleData.remove(item)
                        }
                    }

                    model.saveData(visibleData, hiddenData)
                }
                else -> {

                }
            }
        }
    }

    private fun moveItem(fromIndex: Int, toIndex: Int) {
        val visibleData = model.getVisibleData()
        val hiddenData = model.getHiddenData()

        if (toIndex in 0 until visibleData.size - 1) {

            val itemPosFrom = visibleData[fromIndex].itemPosition
            val itemPosTo = visibleData[toIndex].itemPosition

            Collections.swap(visibleData, fromIndex, toIndex)

            if (fromIndex < toIndex) {

                for (i in itemPosFrom - 1 downTo itemPosTo) {
                    val item: ItemView? = visibleData.find { it.itemPosition == i }
                    if (item != null) {
                        item.itemPosition++
                    } else {
                        hiddenData.find { it.itemPosition == i }?.let {
                            it.itemPosition++
                        }
                    }
                }

                visibleData[toIndex].itemPosition -= (itemPosFrom - itemPosTo)

            } else {

                for (i in itemPosFrom + 1..itemPosTo) {
                    val item: ItemView? = visibleData.find { it.itemPosition == i }
                    if (item != null) {
                        item.itemPosition--
                    } else {
                        hiddenData.find { it.itemPosition == i }?.let {
                            it.itemPosition--
                        }
                    }
                }

                visibleData[toIndex].itemPosition += (itemPosTo - itemPosFrom)
            }

            model.saveData(visibleData, hiddenData)
        }
    }

    private fun deleteItem(position: Int) {
        val visibleData: MutableList<ItemView> = model.getVisibleData()
        deletedItem = visibleData[position]

        // remove item
        visibleData.remove(deletedItem)

        model.saveData(visibleData, model.getHiddenData())

        timer = if (timer == null) {
            startFabRestoreVisibilityTimer()
        } else {
            timer?.cancel()
            startFabRestoreVisibilityTimer()
        }

    }

    private fun restoreItem() {
        deletedItem?.let {
            val restoredData = model.getVisibleData()
            val hiddenData = model.getHiddenData()
            val itemsIsHidden = model.getSuccessItemsIsHidden()
            if ((deletedItem as ViewBase).itemIsSuccess && itemsIsHidden) {
                if (hiddenData.find { it.itemPosition == (deletedItem as ViewBase).itemPosition } == null) {
                    hiddenData.add(deletedItem as ViewBase)
                    model.saveData(model.getVisibleData(), hiddenData)
                    view.updateTextSuccessInfo(getCountItemsIsSuccess())
                }
            } else {
                if (restoredData.find { it == deletedItem } == null) {
                    restoredData.add((deletedItem as ViewBase))
                    restoredData.sortBy { it.itemPosition.inv() }
                    model.saveData(restoredData, model.getHiddenData())
                }
            }

            deletedItem = null
            view.setFabRestoreVisibilityHidden()
            timer?.cancel()
        }
    }

    private fun getCountItemsIsSuccess(): Int {
        val visibleData = model.getVisibleData()
        val hiddenData = model.getHiddenData()

        var countItemIsSuccess = 0

        visibleData.forEach {
            if (it is ViewBase && it.itemIsSuccess) {
                countItemIsSuccess++
            }
        }

        hiddenData.forEach { item ->
            if (item is ViewBase && item.itemIsSuccess) {
                countItemIsSuccess++
            }
        }
        return countItemIsSuccess
    }

    private fun startTaskFragment(itemId: String) {
        replaceFragment(view.getMainFragmentActivity(), TasksFragment(), itemId, true)
    }

    private fun startLockSwipeTimer() {
        view.detachItemTouchHelperToRecyclerView()

        val duration = 200L
        val tick = 200L
        val timer = object : CountDownTimer(duration, tick) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                view.attachItemTouchHelperToRecyclerView()
            }

        }
        timer.start()
    }

    private fun startFabRestoreVisibilityTimer(): CountDownTimer {
        val duration = 5000L
        val tick = 5000L
        val timer = object : CountDownTimer(duration, tick) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                view.setFabRestoreVisibilityHidden()
            }

        }
        view.setFabRestoreVisibilityShown()
        timer.start()
        return timer
    }

    override fun onDestroy() {

    }

}