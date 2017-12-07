package com.trading212.diverserecycleradapter

import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.support.annotation.IdRes
import android.support.annotation.IntRange
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter.ViewHolder
import java.util.*

/**
 * **Note:** Use [NeatLinearLayoutManager] in the hosting RecyclerView([RecyclerView.setLayoutManager])
 * in order to have [DiverseRecyclerAdapter] work correctly. Otherwise [ViewHolder.onAttachedToWindow]/[ViewHolder.onDetachedFromWindow]
 * will not be called when the hosting RecyclerView is attached to/detached from window
 */
class DiverseRecyclerAdapter : RecyclerView.Adapter<DiverseRecyclerAdapter.ViewHolder<*>>(), Filterable {

    private val recyclerItems = ArrayList<RecyclerItem<*, *>>()

    // Used for optimizing the search of RecyclerItem by itemType
    private val itemTypeItemMap = HashMap<Int, RecyclerItem<*, *>>()

    private var filter: Filter? = null

    private var onItemClickListener: OnItemClickListener? = null

    override fun getItemCount(): Int = recyclerItems.size

    override fun getItemViewType(position: Int): Int = getItem<RecyclerItem<*, *>>(position).itemType

    override fun getItemId(position: Int): Long = getItem<RecyclerItem<*, *>>(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
        Log.i(TAG, "Creating ViewHolder of type " + viewType)

        val recyclerItem = itemTypeItemMap[viewType]
        if (recyclerItem != null) {
            return recyclerItem.createViewHolder(parent)
        }

        throw IllegalStateException("Unsupported item view type: $viewType")
    }

    override fun onBindViewHolder(holder: ViewHolder<*>, position: Int) {
        Log.i(TAG, "Binding data for ViewHolder type ${holder.itemViewType} at position $position")

        val item = getItem<RecyclerItem<*, *>>(position)
        holder.bindDataInternal(item.data, onItemClickListener, item)
    }

    override fun onViewRecycled(holder: ViewHolder<*>?) {
        holder?.unbind()

        super.onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder<*>?) {
        super.onViewAttachedToWindow(holder)

        holder?.onAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder<*>?) {
        super.onViewDetachedFromWindow(holder)

        holder?.onDetachedFromWindow()
    }

    override fun getFilter(): Filter? = filter

    fun setFilter(filter: Filter) {
        this.filter = filter
    }

    /**
     * Listener to receive item click events.
     *
     * **NOTE:** This listener overrides click events on the [ViewHolder]'s itemView
     */
    fun setOnItemCLickListener(itemClickListener: OnItemClickListener) {
        this.onItemClickListener = itemClickListener
    }

    /**
     * @param position The position of the item in the adapter
     *
     * @return The [RecyclerItem] at the specified position in the adapter
     */
    fun <RI : RecyclerItem<*, *>> getItem(position: Int): RI = recyclerItems[position] as RI

    /**
     * Adds new [RecyclerItem] at the bottom of the list
     *
     * @param item The item to add
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    fun addItem(item: RecyclerItem<*, *>, notifyAdapter: Boolean) {
        insertItem(itemCount, item, notifyAdapter)
    }


    /**
     * Adds a [List] of [RecyclerItem]s at the bottom of the list
     *
     * @param items The items to add
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    fun addItems(items: List<RecyclerItem<*, *>>, notifyAdapter: Boolean) {
        insertItems(itemCount, items, notifyAdapter)
    }

    /**
     * Adds an array of [RecyclerItem]s at the bottom of the list
     *
     * @param notifyAdapter Whether to notify the adapter about the change
     * @param items The items to add
     */
    fun addItems(notifyAdapter: Boolean, vararg items: RecyclerItem<*, *>) {
        insertItems(itemCount, Arrays.asList<RecyclerItem<*, *>>(*items), notifyAdapter)
    }

    /**
     * Inserts the [RecyclerItem] at the specified position
     *
     * @param position The position to insert the item at
     * @param item The item to insert
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    fun insertItem(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                   item: RecyclerItem<*, *>, notifyAdapter: Boolean) {

        rangeCheckForAdd(position)

        insertItemInternal(position, item)

        if (notifyAdapter) {
            notifyItemInserted(position)
        }
    }

    /**
     * Inserts a [List] of [RecyclerItem]s at the specified position
     *
     * @param position The position to insert the items at
     * @param items The items to insert
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    fun insertItems(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                    items: List<RecyclerItem<*, *>>,
                    notifyAdapter: Boolean) {

        rangeCheckForAdd(position)

        if (items.isEmpty()) {
            for (i in items.indices) {
                insertItemInternal(position + i, items[i])
            }

            if (notifyAdapter) {
                notifyItemRangeInserted(position, items.size)
            }
        } else {
            Log.e(TAG, "Trying to insert empty or null list at position $position")
        }
    }

    /**
     * Inserts an array of [RecyclerItem]s at the specified position
     *
     * @param position The position to insert the items at
     * @param notifyAdapter Whether to notify the adapter about the change
     * @param items The items to insert
     */
    fun insertItems(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                    notifyAdapter: Boolean,
                    vararg items: RecyclerItem<*, *>) {

        insertItems(position, Arrays.asList<RecyclerItem<*, *>>(*items), notifyAdapter)
    }

    /**
     * Removes the [RecyclerItem] at the specified position
     *
     * @param position The position of the item to be removed
     * @param notifyAdapter Whether to notify the adapter about the change
     *
     * @return The removed [RecyclerItem] or null if not found
     */
    fun removeItem(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                   notifyAdapter: Boolean): RecyclerItem<*, *>? {

        val removedItem = removeItemInternal(position)
        if (removedItem != null && notifyAdapter) {
            notifyItemRemoved(position)
        }

        return removedItem
    }

    /**
     * Removes a range of [RecyclerItem]s from the list
     *
     * @param startPosition The position of the first item to be removed
     * @param count The items count including the first to be removed
     * @param notifyAdapter Whether to notify the adapter about the change
     *
     * @return The list of removed items. If there were no removed items the list will be empty
     */
    fun removeRange(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) startPosition: Int,
                    @IntRange(from = 1, to = Integer.MAX_VALUE.toLong()) count: Int,
                    notifyAdapter: Boolean): List<RecyclerItem<*, *>> {

        if (startPosition < 0 || startPosition + count > itemCount) {
            throw IndexOutOfBoundsException("Invalid deletion range ($startPosition, $count). Adapter count is $itemCount")
        }

        val removedItems = ArrayList<RecyclerItem<*, *>>(count)
        for (i in 0 until count) {
            val removedItem = removeItemInternal(startPosition)
            if (removedItem != null) {
                removedItems.add(removedItem)
            }
        }

        if (notifyAdapter) {
            notifyItemRangeRemoved(startPosition, count)
        }

        return removedItems
    }

    /**
     * Removes all [RecyclerItem]s from the list
     *
     * @param notifyAdapter Whether to notify the adapter about the change
     *
     * @return The list of removed items. If there were no removed items the list will be empty
     */
    fun removeAll(notifyAdapter: Boolean): List<RecyclerItem<*, *>> = removeRange(0, itemCount, notifyAdapter)

    /**
     * Moves the [RecyclerItem] at the `fromPosition` to the position, specified by `toPosition`
     *
     * @param fromPosition The current position of the [RecyclerItem]
     * @param toPosition The final position of the [RecyclerItem]
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    fun moveItem(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) fromPosition: Int,
                 @IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) toPosition: Int,
                 notifyAdapter: Boolean) {

        Collections.swap(recyclerItems, fromPosition, toPosition)

        if (notifyAdapter) {
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    /**
     * Finds the position of the first item with the specified viewType
     *
     * @param viewType The view type of the [RecyclerItem]
     *
     * @return The index of the first [RecyclerItem] with the specified viewType or -1 if not found
     */
    fun findFirstViewTypePosition(viewType: Int): Int = recyclerItems.indices.firstOrNull {
        getItem<RecyclerItem<*, *>>(it).itemType == viewType
    } ?: -1

    /**
     * Finds the position of the last item with the specified viewType
     *
     * @param viewType The view type of the [RecyclerItem]
     *
     * @return The index of the last [RecyclerItem] with the specified viewType or -1 if not found
     */
    fun findLastViewTypePosition(viewType: Int): Int = recyclerItems.indices.reversed().firstOrNull {
        getItem<RecyclerItem<*, *>>(it).itemType == viewType
    } ?: -1

    private fun insertItemInternal(position: Int, item: RecyclerItem<*, *>?) {
        if (item != null) {
            recyclerItems[position] = item

            if (!itemTypeItemMap.containsKey(item.itemType)) {
                itemTypeItemMap[item.itemType] = item
            }
        } else {
            Log.e(TAG, "Trying to insert null item at position $position")
        }
    }

    private fun removeItemInternal(position: Int): RecyclerItem<*, *>? {
        if (position < 0 || position >= itemCount) {
            return null
        }

        val removed = recyclerItems.removeAt(position)

        if (removed != null && findFirstViewTypePosition(removed.itemType) < 0) {
            itemTypeItemMap.remove(removed.itemType)
        }

        return removed
    }

    private inline fun rangeCheckForAdd(position: Int) {
        if (position < 0 || position > itemCount) {
            throw IndexOutOfBoundsException("Invalid insertion position: $position. Adapter size is $itemCount")
        }
    }

    interface OnItemClickListener {

        /**
         * Called on itemView click event
         *
         * @param v The itemView of the [ViewHolder]
         * @param item The [RecyclerItem] of the clicked view
         */
        fun onItemClicked(v: View, item: RecyclerItem<*, *>)

        /**
         * Called on item touch event
         *
         * @param v The itemView of the [ViewHolder]
         * @param event The motion event on the item
         * @param item The [RecyclerItem] of the touched view
         *
         * @return true if the event is handled, false if the event is unhandled
         * **NOTE:** Returning true will stop triggering of subsequent gesture events like [View.OnClickListener.onClick]
         */
        fun onItemTouched(v: View, event: MotionEvent, item: RecyclerItem<*, *>): Boolean = false
    }

    abstract class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * `true` if the [ViewHolder]'s itemView is attached to the parent [RecyclerView]
         * i.e. visible, `false` otherwise
         */
        protected var isAttached: Boolean = false
            private set

        /**
         * You should update the UI of the recycler item here.
         *
         * **NOTE:** itemView click listener is preserved for adapter internal usage. If you need itemView click
         * listener, you should override [ViewHolder.onItemViewClicked] or use child view of the itemView as click handler
         *
         * @param data The data object holding the information for the [ViewHolder]
         */
        abstract fun bindTo(data: T)

        /**
         * Called when the itemView of this [ViewHolder] has been attached to the parent
         *
         * This can be used as a reasonable signal that the view is about to be seen by the user. If the [ViewHolder] previously
         * freed any resources in [ViewHolder.onDetachedFromWindow] those resources should be restored here.
         */
        @CallSuper
        open fun onAttachedToWindow() {
            isAttached = true
        }

        /**
         * Called when the itemView of this [ViewHolder] has been detached from it's parent
         *
         * Becoming detached from the window is not necessarily a permanent condition. The consumer of an Adapter's views may
         * choose to cache views offscreen while they are not visible, attaching an detaching them as appropriate.
         */
        @CallSuper
        open fun onDetachedFromWindow() {
            isAttached = false
        }

        /**
         * Called just before this [ViewHolder] instance is going to be recycled.
         *
         * A view is recycled when a [android.support.v7.widget.RecyclerView.LayoutManager] decides that it no longer
         * needs to be attached to its parent [RecyclerView]. If an item view has large or expensive
         * data bound to it such as large bitmaps, this may be a good place to release those resources.
         *
         * [RecyclerView] calls this method right before clearing ViewHolder's internal data and sending it to
         * RecycledViewPool. This way, if ViewHolder was holding valid information before being recycled, you can call
         * [ViewHolder.getAdapterPosition] to get its adapter position.
         */
        open fun unbind() {}

        /**
         * Called when the itemView of the [ViewHolder] has been clicked
         */
        protected open fun onItemViewClicked() {}

        /**
         * @return the [View] for the given resource id and tries to cast it to the inferred type
         */
        @CheckResult
        protected fun <U : View> findViewById(@IdRes id: Int): U? = itemView.findViewById(id) as U

        internal fun bindDataInternal(data: T, onItemClickListener: OnItemClickListener?, item: RecyclerItem<*, *>) {

            bindTo(data)

            onItemClickListener?.let {
                itemView.setOnTouchListener { v, event ->
                    it.onItemTouched(v, event, item)
                }
            }

            itemView.setOnClickListener { v ->
                onItemClickListener?.onItemClicked(v, item)
                onItemViewClicked()
            }
        }

        interface Draggable {

            /**
             * Called before attempting a drag action. Use to dynamically enable/disable drag for the current item
             *
             * @return true to enable drag, false otherwise
             */
            val isDragEnabled: Boolean

            /**
             * Called when a drag action starts on the view created by this [ViewHolder]
             */
            fun onDragStart()

            /**
             * Called when the drag action on the view created by this [ViewHolder] has finished
             */
            fun onDragFinish()
        }
    }

    abstract class RecyclerItem<T, VH : ViewHolder<T>> {

        /**
         * The stable ID for the item. If [hasStableIds] would return false this property should have [RecyclerView.NO_ID] value,
         * which is the default value
         */
        open val id: Long
            get() = RecyclerView.NO_ID

        /**
         * An unique [Int] which will be used as viewType for the category(all items of the same type)
         *
         * **NOTE:** The value should be unique across all [RecyclerItem] categories in one instance of [DiverseRecyclerAdapter].
         * If you duplicate the viewTypes the [RecyclerView] will show invalid data
         *
         * @see android.support.v7.widget.RecyclerView.Adapter.getItemViewType
         */
        @get:IntRange(from = 0, to = Integer.MAX_VALUE.toLong())
        abstract val itemType: Int

        /**
         * An object, containing the data to be displayed in related [ViewHolder]. The same object will be passed in [ViewHolder.bindTo]
         */
        abstract val data: T?

        /**
         * You should create a new [ViewHolder] for the corresponding [RecyclerItem]
         *
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position
         *
         * @return A new [ViewHolder] that holds the [View] for the corresponding [RecyclerItem]
         *
         * @see android.support.v7.widget.RecyclerView.Adapter.onCreateViewHolder
         */
        abstract fun createViewHolder(parent: ViewGroup): VH
    }

    companion object {

        private val TAG = DiverseRecyclerAdapter::class.java.simpleName
    }
}
