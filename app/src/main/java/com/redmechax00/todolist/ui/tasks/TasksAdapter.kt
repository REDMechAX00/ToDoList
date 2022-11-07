package com.redmechax00.todolist.ui.tasks

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.utils.MyDiffUtilCallback
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.HolderFactory
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderDeadlineItem
import com.redmechax00.todolist.recycler_view.holders.tasks.HolderDescItem
import com.redmechax00.todolist.recycler_view.views.ItemView


class TasksAdapter(
    private val mOnItemClickListener: OnItemClickListener,
    private val mTextChangeListener: HolderDescItem.TextChangeListener,
    private val mOnCheckedChangeListener: HolderDeadlineItem.OnCheckedChangeListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listHolders: MutableList<ItemHolder> = mutableListOf()
    private val listViews: AsyncListDiffer<ItemView> =
        AsyncListDiffer(this, MyDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemHolder).drawItem(listViews.currentList[position])
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        holder as ItemHolder
        holder.setOnItemClickListener(mOnItemClickListener)
        when (holder) {
            is HolderDescItem -> {
                holder.addTextChangeListener(mTextChangeListener)
            }
            is HolderDeadlineItem -> {
                holder.setOnCheckedChangeListener(mOnCheckedChangeListener)
            }
        }
        listHolders.add(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        holder as ItemHolder
        holder.onDetach()
        listHolders.remove(holder)
    }

    override fun getItemViewType(position: Int): Int = listViews.currentList[position].getTypeView()

    override fun getItemCount() = listViews.currentList.size

    fun updateItems(tasksItems: MutableList<ItemView>) {
        listViews.submitList(tasksItems)
    }

    fun onDestroy() {
        listHolders.forEach { holder ->
            holder.onDetach()
        }
    }
}
