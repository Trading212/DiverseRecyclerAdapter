package com.trading212.diverserecycleradapter.drag

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

/**
 * Created by svetlin on 29.01.18.
 */
class DragItemTouchHelperCallback(private val adapter: DiverseRecyclerAdapter) : ItemTouchHelper.Callback() {

    var onItemMoveListener: OnItemMoveListener? = null

    var isDragWithLongPressEnabled: Boolean = true

    var dragFlags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        // Do nothing. Not supported for now
    }

    override fun isLongPressDragEnabled(): Boolean = isDragWithLongPressEnabled && adapter.itemCount > 1

    override fun getMovementFlags(recyclerView: RecyclerView?,
                                  viewHolder: RecyclerView.ViewHolder?): Int {

        return if (viewHolder is Draggable && viewHolder.isDraggable()) {
            val dragFlags = this.dragFlags
            makeMovementFlags(dragFlags, 0)
        } else {
            makeMovementFlags(0, 0)
        }
    }

    override fun onMove(recyclerView: RecyclerView?,
                        source: RecyclerView.ViewHolder?,
                        target: RecyclerView.ViewHolder?): Boolean {

        if (source is Draggable && target is Draggable && target.isDraggable()) {

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
        }
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is Draggable) {
            viewHolder.onDragFinish()
        }
    }

    interface OnItemMoveListener {

        fun onItemMoved(fromPosition: Int, toPosition: Int)
    }
}