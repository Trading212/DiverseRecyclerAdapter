package com.trading212.diverserecycleradapter.drag

/**
 * Created by svetlin on 29.01.18.
 */
interface Draggable {

    /**
     * Called when a drag action starts on the view created by this {@link ViewHolder}
     */
    fun onDragStart()

    /**
     * Called when the drag action on the view created by this {@link ViewHolder} has finished
     */
    fun onDragFinish()

    /**
     * Checked before attempting a drag action. Use to dynamically enable/disable drag for the current item
     *
     * @return true to enable drag, false otherwise
     */
    fun isDraggable(): Boolean
}