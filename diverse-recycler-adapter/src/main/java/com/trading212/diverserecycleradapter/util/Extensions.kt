@file:JvmName("DiverseRecyclerAdapterUtil")

package com.trading212.diverserecycleradapter.util

import androidx.recyclerview.widget.DiffUtil
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter.RecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter.ViewHolder

fun DiverseRecyclerAdapter.replaceItems(items: List<RecyclerItem<*, ViewHolder<*>>>) {

    val old = ArrayList(recyclerItems)
    val new = ArrayList(items)

    removeAll(false)
    addItems(items, false)

    DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                old[oldItemPosition].type == new[newItemPosition].type
                        && old[oldItemPosition].id == new[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                old[oldItemPosition].data == new[newItemPosition].data

    }).dispatchUpdatesTo(this)
}