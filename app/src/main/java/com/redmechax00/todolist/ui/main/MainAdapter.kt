package com.redmechax00.todolist.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.todolist.utils.MyDiffUtilCallback
import com.redmechax00.todolist.recycler_view.OnItemClickListener
import com.redmechax00.todolist.recycler_view.holders.HolderFactory
import com.redmechax00.todolist.recycler_view.holders.ItemHolder
import com.redmechax00.todolist.recycler_view.holders.main.HolderBaseItem
import com.redmechax00.todolist.recycler_view.views.ItemView


class MainAdapter(
    private val mOnItemClickListener: OnItemClickListener,
    private val mOnCheckedChangeListener: HolderBaseItem.OnCheckedChangeListener
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
            is HolderBaseItem -> {
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

    fun updateItems(visibleItems: MutableList<ItemView>) {
        listViews.submitList(visibleItems)
    }

    fun onDestroy() {
        listHolders.forEach { holder ->
            holder.onDetach()
        }
    }
}
