package com.redmechax00.todolist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.SimpleItemAnimator
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.redmechax00.todolist.App
import com.redmechax00.todolist.R
import com.redmechax00.todolist.databinding.MainFragmentBinding
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.main.HolderBaseItem
import com.redmechax00.todolist.recycler_view.views.ItemView
import com.redmechax00.todolist.ui.Contract
import com.redmechax00.todolist.ui.TodoItemsViewModel
import com.redmechax00.todolist.utils.ITEM_TYPE_BASE
import com.redmechax00.todolist.utils.MyItemTouchHelper

class MainFragment : Fragment(), Contract.MainView,
    MyItemTouchHelper.GoToMainFragment,
    OnItemClickListener, HolderBaseItem.OnCheckedChangeListener {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: MainPresenter
    private lateinit var viewModel: TodoItemsViewModel

    //Toolbar
    private lateinit var toolbar: Toolbar
    private lateinit var btnToolbarVisibility: LottieAnimationView

    //Collapsing toolbar
    private lateinit var mainAppBarLayout: AppBarLayout
    private lateinit var textSuccessInfo: TextView
    private lateinit var btnVisibility: LottieAnimationView

    //Floating Buttons
    private lateinit var fabNewItem: FloatingActionButton
    private lateinit var fabRestoreItem: FloatingActionButton

    //RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    //ItemTouchHelper
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val v = binding.root

        viewModel = ViewModelProvider(requireActivity())[TodoItemsViewModel::class.java]
        presenter = MainPresenter(this, viewModel)

        return v
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun initView() {
        //Toolbar
        toolbar = binding.mainToolbar
        btnToolbarVisibility = binding.mainToolbarBtnVisibility
        tuneToolbar()

        //Collapsing toolbar
        mainAppBarLayout = binding.mainAppBarLayout
        textSuccessInfo = binding.mainTextSuccessInfo
        btnVisibility = binding.mainBtnVisibility
        setCollapsingToolbarItemsListeners()

        //Floating Buttons
        fabNewItem = binding.mainFabNewItem
        fabRestoreItem = binding.mainFabRestoreItem
        setFabsListeners()

        //RecyclerView
        recyclerView = binding.mainRecyclerView
        adapter = MainAdapter(this, this)
        tuneRecyclerView()

        initItemTouchHelper()
    }

    override fun getMainFragment(): MainFragment = this

    override fun getMainFragmentActivity(): AppCompatActivity =
        requireActivity() as AppCompatActivity

    override fun getMainLifecycleOwner(): LifecycleOwner = viewLifecycleOwner

    private fun initItemTouchHelper() {
        itemTouchHelperCallback = MyItemTouchHelper(
            (activity as AppCompatActivity),
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START or ItemTouchHelper.END,
            this
        )
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun tuneToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun tuneRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    override fun updateAdapter(newItems: MutableList<ItemView>) {
        adapter.updateItems(newItems)
    }

    override fun attachItemTouchHelperToRecyclerView() {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun detachItemTouchHelperToRecyclerView() {
        itemTouchHelper.attachToRecyclerView(null)
    }

    private fun setCollapsingToolbarItemsListeners() {

        mainAppBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            presenter.onOffsetChange(appBarLayout, verticalOffset)
        }

        btnToolbarVisibility.setOnClickListener {
            presenter.onBtnVisibilityClick()
        }

        btnVisibility.setOnClickListener {
            presenter.onBtnVisibilityClick()
        }
    }

    private fun setFabsListeners() {
        fabNewItem.setOnClickListener {
            presenter.onFabNewClick()
        }

        fabRestoreItem.setOnClickListener {
            presenter.onFabRestoreClick()
        }
    }

    override fun setBtnVisibilityHidden() {
        btnToolbarVisibility.progress = 1f
        btnVisibility.progress = 1f
    }

    override fun setBtnVisibilityShown() {
        btnToolbarVisibility.progress = 0f
        btnVisibility.progress = 0f
    }

    override fun setBtnVisibilityAnimateHidden() {
        btnToolbarVisibility.speed = 1f
        btnToolbarVisibility.playAnimation()
        btnVisibility.speed = 1f
        btnVisibility.playAnimation()
    }

    override fun setBtnVisibilityAnimateShown() {
        btnToolbarVisibility.speed = -1f
        btnToolbarVisibility.playAnimation()
        btnVisibility.speed = -1f
        btnVisibility.playAnimation()
    }

    override fun setAlphaForCollapsingToolbarItems(
        alphaForToolbarItems: Float,
        alphaForCollapsingItems: Float
    ) {
        btnToolbarVisibility.alpha = alphaForToolbarItems
        btnVisibility.alpha = alphaForCollapsingItems
        textSuccessInfo.alpha = alphaForCollapsingItems
    }

    override fun setVisibilityForCollapsingToolbarItems(
        visibilityForToolbarItems: Int,
        visibilityForCollapsingItems: Int
    ) {
        btnToolbarVisibility.visibility = visibilityForToolbarItems
        btnVisibility.visibility = visibilityForCollapsingItems
    }

    override fun setFabRestoreVisibilityHidden() {
        fabRestoreItem.hide()
    }

    override fun setFabRestoreVisibilityShown() {
        fabRestoreItem.show()
    }

    override fun updateTextSuccessInfo(countSuccessItems: Int) {
        val textCompleted = "${App.Strings.get(R.string.text_completed)} $countSuccessItems"
        textSuccessInfo.text = textCompleted
    }

    override fun onItemClick(holder: ViewHolder, itemView: ItemView) {
        presenter.onItemClick(holder, itemView)
    }

    override fun onCheckBoxChange(position: Int, itemIsSuccess: Boolean) {
        presenter.onCheckBoxChange(position, itemIsSuccess)
    }

    override fun onDrag(fromIndex: Int, toIndex: Int) {
        presenter.onDrag(fromIndex, toIndex)
    }

    override fun onSwiped(direction: Int, holder: ViewHolder) {
        when (holder.itemViewType) {
            ITEM_TYPE_BASE -> {
                presenter.onSwiped()

                val position = holder.absoluteAdapterPosition
                when (direction) {
                    //Action - Delete
                    ItemTouchHelper.START -> {
                        presenter.onActionDelete(position)
                    }

                    //Action - Check
                    ItemTouchHelper.END -> {
                        presenter.onActionCheck(position)
                    }
                }
            }
        }
    }

    override fun onActionsEnd() {
        presenter.onActionEnd()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
        adapter.onDestroy()
        fabNewItem.setOnClickListener(null)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(null)
    }

}