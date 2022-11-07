package com.redmechax00.todolist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.recycler_view.views.main.ViewBase
import com.redmechax00.todolist.ui.main.MainFragment
import com.redmechax00.todolist.ui.tasks.TasksFragment
import java.util.*

interface Contract {
    interface MainView {
        fun initView()

        fun getMainFragment(): MainFragment

        fun getMainFragmentActivity(): AppCompatActivity

        fun getMainLifecycleOwner(): LifecycleOwner

        fun updateAdapter(newItems: MutableList<ItemView>)

        fun attachItemTouchHelperToRecyclerView()

        fun detachItemTouchHelperToRecyclerView()

        fun setBtnVisibilityHidden()

        fun setBtnVisibilityShown()

        fun setBtnVisibilityAnimateHidden()

        fun setBtnVisibilityAnimateShown()

        fun setAlphaForCollapsingToolbarItems(
            alphaForToolbarItems: Float,
            alphaForCollapsingItems: Float
        )

        fun setVisibilityForCollapsingToolbarItems(
            visibilityForToolbarItems: Int,
            visibilityForCollapsingItems: Int
        )

        fun setFabRestoreVisibilityHidden()

        fun setFabRestoreVisibilityShown()

        fun updateTextSuccessInfo(countSuccessItems: Int)
    }

    interface MainPresenter {
        fun onResume()

        fun onResumeBtnVisibility()

        fun onOffsetChange(appBarLayout: AppBarLayout, verticalOffset: Int)

        fun onBtnVisibilityClick()

        fun onFabNewClick()

        fun onFabRestoreClick()

        fun onItemClick(holder: RecyclerView.ViewHolder, itemView: ItemView)

        fun onCheckBoxChange(position: Int, itemIsSuccess: Boolean)

        fun onDrag(fromIndex: Int, toIndex: Int)

        fun onSwiped()

        fun onActionDelete(position: Int)

        fun onActionCheck(position: Int)

        fun onActionEnd()

        fun onDestroy()
    }

    interface TasksView {
        fun initView()

        fun popBackStack()

        fun getTasksFragment(): TasksFragment

        fun getTasksFragmentActivity(): AppCompatActivity

        fun getTasksLifecycleOwner(): LifecycleOwner

        fun getTasksArguments(): Bundle?

        fun updateAdapter(newItems: MutableList<ItemView>)

        fun showImportancePopupMenu(holder: RecyclerView.ViewHolder)

        fun showDeadlineDatePickerDialog(c: Calendar)
    }

    interface TasksPresenter {
        fun onResume()

        fun onSavePressed()

        fun onClosePressed()

        fun onTextChanged(s: CharSequence?)

        fun onItemImportanceClick(holder: RecyclerView.ViewHolder)

        fun onItemDeadlineClick()

        fun onSwitchBoxDeadlineChange(isChecked: Boolean)

        fun onItemDeleteClick()

        fun onDeadlineDateDialogPositiveClick(year: Int, month: Int, dayOfMonth: Int)

        fun onDeadlineDateDialogNegativeClick()

        fun onImportancePopupMenuNoClick()

        fun onImportancePopupMenuLowClick()

        fun onImportancePopupMenuHighClick()

        fun onDestroy()
    }

    interface Model {

        fun saveData(newDataVisible: MutableList<ItemView>, newDataHidden: MutableList<ItemView>)

        fun getVisibleData(): MutableList<ItemView>

        fun getHiddenData(): MutableList<ItemView>

        fun setCurrentFragment(fragment: Fragment)

        fun getCurrentFragment(): Fragment?

        fun setEditedItem(editedItem: ViewBase?)

        fun getEditedItem(): ViewBase?

        fun setSuccessItemsIsHidden(successItemsIsHidden: Boolean)

        fun getSuccessItemsIsHidden(): Boolean
    }
}