package com.trading212.diverserecycleradapter.drag

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

/**
 * Created by svetlin on 29.01.18.
 */
class DragItemTouchHelperCallback(private val adapter: DiverseRecyclerAdapter) : ItemTouchHelper.Callback() {

    var onItemMoveListener: OnItemMoveListener? = null

    var onItemDragListener: OnItemDragListener? = null

    var isDragWithLongPressEnabled: Boolean = true

    var dragFlags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    override fun isItemViewSwipeEnabled(): Boolean = false // Swipe is not supported, yet

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Do nothing. Not supported yet
    }

    override fun isLongPressDragEnabled(): Boolean = isDragWithLongPressEnabled && adapter.itemCount > 1

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {

        return if (viewHolder is Draggable && viewHolder.isDragEnabled()) {
            makeMovementFlags(dragFlags, 0)
        } else {
            makeMovementFlags(0, 0)
        }
    }

    override fun onMove(recyclerView: RecyclerView,
                        source: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {

        if (source is Draggable && target is Draggable && target.isDragEnabled()) {

            val fromPosition = source.adapterPosition
            val toPosition = target.adapterPosition

            if (fromPosition != toPosition) {

                adapter.moveItem(fromPosition, toPosition, true)

                onItemMoveListener?.onItemMoved(fromPosition, toPosition)

                return true
            }
        }

        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder is Draggable) {
            viewHolder.onDragStart()

            onItemDragListener?.onItemDragStart(viewHolder.adapterPosition)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is Draggable) {
            viewHolder.onDragFinish()

            onItemDragListener?.onItemDragFinish(viewHolder.adapterPosition)
        }
    }

    interface OnItemMoveListener {

        /**
         * Called when item has been dragged form [fromAdapterPosition] to [toAdapterPosition]
         */
        fun onItemMoved(fromAdapterPosition: Int, toAdapterPosition: Int)
    }

    interface OnItemDragListener {

        /**
         * Called when a drag action starts on the item at [adapterPosition]
         */
        fun onItemDragStart(adapterPosition: Int)

        /**
         * Called when the drag action on the item at [adapterPosition] has finished
         */
        fun onItemDragFinish(adapterPosition: Int)
    }
}