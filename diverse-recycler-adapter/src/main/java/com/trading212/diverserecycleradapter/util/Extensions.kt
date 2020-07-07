@file:JvmName("DiverseRecyclerAdapterUtil")

package com.trading212.diverserecycleradapter.util

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter.RecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter.ViewHolder

@JvmOverloads
fun DiverseRecyclerAdapter.replaceItems(
        items: List<RecyclerItem<*, ViewHolder<*>>>,
        itemChangePayloadProvider: (itemType: Int, oldData: Any?, newData: Any?) -> Any? = { _, _, _ -> null }
) {

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

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
                itemChangePayloadProvider(
                        new[newItemPosition].type,
                        old[oldItemPosition].data,
                        new[newItemPosition].data
                )

    }).dispatchUpdatesTo(this)
}

inline fun DiverseRecyclerAdapter.onItemClicked(crossinline onClick: (v: View, position: Int) -> Unit) {
    addOnItemActionListener(object : DiverseRecyclerAdapter.OnItemActionListener() {
        override fun onItemClicked(v: View, position: Int) {
            onClick(v, position)
        }
    })
}

inline fun DiverseRecyclerAdapter.onItemLongClicked(crossinline onLongClick: (v: View, position: Int) -> Boolean) {
    addOnItemActionListener(object : DiverseRecyclerAdapter.OnItemActionListener() {
        override fun onItemLongClicked(v: View, position: Int): Boolean {
            return onLongClick(v, position)
        }
    })
}

inline fun DiverseRecyclerAdapter.onItemTouched(
        crossinline onTouch: (v: View, event: MotionEvent, position: Int) -> Unit
) {
    addOnItemActionListener(object : DiverseRecyclerAdapter.OnItemActionListener() {
        override fun onItemTouched(v: View, event: MotionEvent, position: Int): Boolean {
            return onItemTouched(v, event, position)
        }
    })
}

inline fun DiverseRecyclerAdapter.onItemSelectionStateChanged(
        crossinline onSelectionStateChange: (v: View, position: Int, isSelected: Boolean) -> Unit
) {
    addOnItemSelectionStateChangeListener(object : DiverseRecyclerAdapter.OnItemSelectionStateChangeListener {
        override fun onItemSelectionStateChanged(v: View, position: Int, isSelected: Boolean) {
            onSelectionStateChange(v, position, isSelected)
        }
    })
}