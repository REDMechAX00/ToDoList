package com.redmechax00.todolist.ui.tasks

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.redmechax00.todolist.App
import com.redmechax00.todolist.R
import com.redmechax00.todolist.databinding.TasksFragmentBinding
import com.redmechax00.todolist.ui.Contract
import com.redmechax00.todolist.ui.TodoItemsViewModel
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderDeadlineItem
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderDescItem
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.utils.*
import java.util.*


class TasksFragment : Fragment(), Contract.TasksView, OnItemClickListener,
    HolderDescItem.TextChangeListener,
    HolderDeadlineItem.OnCheckedChangeListener {

    private var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: TasksPresenter
    private lateinit var viewModel: TodoItemsViewModel

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarBtnSave: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        val v = binding.root

        viewModel = ViewModelProvider(requireActivity())[TodoItemsViewModel::class.java]
        presenter = TasksPresenter(this, viewModel)

        return v
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun initView() {
        //Toolbar
        toolbar = binding.tasksToolbar
        toolbarBtnSave = binding.tasksToolbarBtnSave
        tuneToolbar()
        setToolbarItemsListeners()

        //RecyclerView
        recyclerView = binding.tasksRecyclerView
        adapter = TasksAdapter(this, this, this)
        tuneRecyclerView()
    }

    override fun popBackStack() {
        parentFragmentManager.popBackStack()
    }

    override fun getTasksFragment(): TasksFragment = this

    override fun getTasksFragmentActivity(): AppCompatActivity =
        requireActivity() as AppCompatActivity

    override fun getTasksLifecycleOwner(): LifecycleOwner = viewLifecycleOwner

    override fun getTasksArguments(): Bundle? = arguments

    private fun tuneToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun tuneRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    override fun updateAdapter(newItems: MutableList<ItemView>) {
        adapter.updateItems(newItems)
    }

    private fun setToolbarItemsListeners() {
        toolbar.setNavigationOnClickListener { onClosePressed() }
        toolbarBtnSave.setOnClickListener { onSavePressed() }
    }

    private fun onSavePressed() {
        presenter.onSavePressed()
    }

    private fun onClosePressed() {
        presenter.onClosePressed()
    }

    override fun onTextChanged(s: CharSequence?) {
        presenter.onTextChanged(s)
    }

    override fun onSwitchBoxChange(isChecked: Boolean) {
        presenter.onSwitchBoxDeadlineChange(isChecked)
    }

    override fun onItemClick(holder: RecyclerView.ViewHolder, itemView: ItemView) {
        when (itemView.getTypeView()) {
            ITEM_TYPE_IMPORTANCE -> {
                presenter.onItemImportanceClick(holder)
            }
            ITEM_TYPE_DEADLINE -> {
                presenter.onItemDeadlineClick()
            }
            ITEM_TYPE_DELETE -> {
                presenter.onItemDeleteClick()
            }
        }
    }

    override fun showImportancePopupMenu(holder: RecyclerView.ViewHolder) {
        val myContext: Context =
            ContextThemeWrapper(context, R.style.menuImportanceNormalText)
        val popup = PopupMenu(myContext, holder.itemView)
        popup.inflate(R.menu.menu_item_importance)

        val popupItems = popup.menu.children
        val popupItemImportanceNo: MenuItem? =
            popupItems.find { it.itemId == R.id.menu_item_importance_no }
        val popupItemImportanceLow: MenuItem? =
            popupItems.find { it.itemId == R.id.menu_item_importance_low }
        val popupItemImportanceHigh: MenuItem? =
            popupItems.find { it.itemId == R.id.menu_item_importance_high }
        val textNo =
            SpannableString(popupItemImportanceNo?.title)
        textNo.setSpan(
            ForegroundColorSpan(
                App.Colors.get(R.color.color_importance_no)
            ), 0, textNo.length, 0
        )
        val textLow =
            SpannableString(popupItemImportanceLow?.title)
        textLow.setSpan(
            ForegroundColorSpan(
                App.Colors.get(R.color.color_importance_low)
            ), 0, textLow.length, 0
        )
        val textHigh =
            SpannableString(popupItemImportanceHigh?.title)
        textHigh.setSpan(
            ForegroundColorSpan(
                App.Colors.get(R.color.color_importance_high)
            ), 0, textHigh.length, 0
        )
        popupItemImportanceNo?.title = textNo
        popupItemImportanceLow?.title = textLow
        popupItemImportanceHigh?.title = textHigh

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.menu_item_importance_no -> {
                    presenter.onImportancePopupMenuNoClick()
                }
                R.id.menu_item_importance_low -> {
                    presenter.onImportancePopupMenuLowClick()
                }
                R.id.menu_item_importance_high -> {
                    presenter.onImportancePopupMenuHighClick()
                }
            }

            true
        }
        popup.show()
    }

    override fun showDeadlineDatePickerDialog(c: Calendar) {
        val currentYear = c.get(Calendar.YEAR)
        val currentMonth = c.get(Calendar.MONTH)
        val currentDay = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            presenter.onDeadlineDateDialogPositiveClick(year, month, dayOfMonth)
        }, currentYear, currentMonth, currentDay)

        val textPositive =
            SpannableString(getString(R.string.text_ok))
        textPositive.setSpan(
            ForegroundColorSpan(
                App.Colors.get(R.color.color_secondary)
            ), 0, textPositive.length, 0
        )
        val textNegative =
            SpannableString(getString(R.string.text_cancel))
        textNegative.setSpan(
            ForegroundColorSpan(
                App.Colors.get(R.color.color_secondary)
            ), 0, textNegative.length, 0
        )

        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, textPositive, dpd)
        dpd.setButton(DialogInterface.BUTTON_NEGATIVE, textNegative) { _, which ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                presenter.onDeadlineDateDialogNegativeClick()
            }
        }

        dpd.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
        adapter.onDestroy()
    }

}