package com.trading212.diverserecycleradapter

import android.support.annotation.CheckResult
import android.support.annotation.IdRes
import android.support.annotation.IntRange
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter.RecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter.ViewHolder
import com.trading212.diverserecycleradapter.layoutmanager.*
import java.util.*

/**
 * RecyclerView adapter, which simplifies building lists of different items, by breaking each item type into
 * separate [RecyclerItem]s, which makes the code easier to write, easier to maintain and better encapsulated
 *
 * **Note:** Use [DiverseLinearLayoutManager], [DiverseGridLayoutManager], [DiverseStaggeredGridLayoutManager] or a subclass
 * in the hosting [RecyclerView.setLayoutManager] in order to have [ViewHolder]'s attach/detach events work correctly.
 * Otherwise [ViewHolder.onDetachedFromWindow] will not be called when the hosting [RecyclerView] is detached from
 * window or it's layout manager changes. Alternatively, you can use any type of layout manager and delegate
 * [RecyclerView.LayoutManager.onAttachedToWindow] and [RecyclerView.LayoutManager.onDetachedFromWindow] to
 * [delegateRecyclerViewAttachedToWindow] and [delegateRecyclerViewDetachedFromWindow] respectively
 */
class DiverseRecyclerAdapter : RecyclerView.Adapter<DiverseRecyclerAdapter.ViewHolder<*>>(), Filterable {

    companion object {

        private val TAG = DiverseRecyclerAdapter::class.java.simpleName
    }

    /**
     * @property onItemClickListener Listener to receive item click events
     */
    var onItemClickListener: OnItemClickListener? = null

    private val recyclerItems = ArrayList<RecyclerItem<*, ViewHolder<*>>>()

    // Used for optimizing the search of RecyclerItem by type
    private val itemTypeItemMap = SparseArray<RecyclerItem<*, ViewHolder<*>>>()

    private var filter: Filter? = null

    override fun getItemCount(): Int = recyclerItems.size

    override fun getItemViewType(position: Int): Int = getItem<RecyclerItem<*, ViewHolder<*>>>(position).type

    override fun getItemId(position: Int): Long = getItem<RecyclerItem<*, ViewHolder<*>>>(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {

        Log.i(TAG, "Creating ViewHolder of type " + viewType)

        // Get an instance of RecyclerItem of the specified type. It will be used as factory for ViewHolders
        val recyclerItem = itemTypeItemMap.get(viewType)
        if (recyclerItem != null) {
            return recyclerItem.createViewHolderInternal(parent)
        }

        throw IllegalStateException("Unsupported item type: $viewType")
    }

    override fun onBindViewHolder(holder: ViewHolder<*>, position: Int) {

        Log.i(TAG, "Binding data for ViewHolder with type ${holder.itemViewType} at position $position")

        val item = getItem<RecyclerItem<*, ViewHolder<*>>>(position)

        holder.bindToInternal(item.data)

        onItemClickListener?.let {
            holder.itemView.setOnTouchListener { v, event ->
                it.onItemTouched(v, event, holder.adapterPosition)
            }
        }

        holder.itemView.setOnClickListener { v ->
            onItemClickListener?.onItemClicked(v, holder.adapterPosition)
            holder.onItemViewClickedInternal()
        }
    }

    override fun onViewRecycled(holder: ViewHolder<*>) {

        Log.i(TAG, "Unbinding ViewHolder with type ${holder.itemViewType} at position ${holder.layoutPosition}")

        holder.unbindInternal()

        super.onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder<*>) {
        // Safety check, in case holder is already attached. This can not happen, for now,
        // but future changes to RecyclerView.Adapter may break this logic
        if (holder.isAttached) return

        super.onViewAttachedToWindow(holder)

        Log.i(TAG, "Item at position ${holder.layoutPosition} attached to window")

        holder.onAttachedToWindowInternal()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder<*>) {
        // Safety check, in case holder is already detached. This can not happen, for now,
        // but future changes to RecyclerView.Adapter may break this logic
        if (!holder.isAttached) return

        holder.onDetachedFromWindowInternal()

        Log.i(TAG, "Item at position ${holder.layoutPosition} detached from window")

        super.onViewDetachedFromWindow(holder)
    }

    override fun getFilter(): Filter? = filter

    fun setFilter(filter: Filter) {
        this.filter = filter
    }

    /**
     * @param position The position of the item in the adapter
     *
     * @return The [RecyclerItem] at the specified position in the adapter
     */
    @Suppress("UNCHECKED_CAST")
    fun <RI : RecyclerItem<*, ViewHolder<*>>> getItem(position: Int): RI = recyclerItems[position] as RI

    /**
     * Adds new [RecyclerItem] at the end of the adapter
     *
     * @param item The item to add
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    @JvmOverloads
    fun addItem(item: RecyclerItem<*, ViewHolder<*>>,
                notifyAdapter: Boolean = true) {

        insertItem(itemCount, item, notifyAdapter)
    }

    /**
     * Adds a [List] of [RecyclerItem]s at the end of the adapter
     *
     * @param items The items to add
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    @JvmOverloads
    fun addItems(items: List<RecyclerItem<*, ViewHolder<*>>>,
                 notifyAdapter: Boolean = true) {

        insertItems(itemCount, items, notifyAdapter)
    }

    /**
     * Adds an array of [RecyclerItem]s at the end of the adapter
     *
     * @param notifyAdapter Whether to notify the adapter about the change
     * @param items The items to add
     */
    @JvmOverloads
    fun addItems(notifyAdapter: Boolean = true,
                 vararg items: RecyclerItem<*, ViewHolder<*>>) {

        insertItems(itemCount, listOf(*items), notifyAdapter)
    }

    /**
     * Inserts the [RecyclerItem] at the specified position
     *
     * @param position The position to insert the item at
     * @param item The item to insert
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    @JvmOverloads
    fun insertItem(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                   item: RecyclerItem<*, ViewHolder<*>>,
                   notifyAdapter: Boolean = true) {

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
    @JvmOverloads
    fun insertItems(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                    items: List<RecyclerItem<*, ViewHolder<*>>>,
                    notifyAdapter: Boolean = true) {

        if (!items.isEmpty()) {
            for (i in items.indices) {
                insertItemInternal(position + i, items[i])
            }

            if (notifyAdapter) {
                notifyItemRangeInserted(position, items.size)
            }
        } else {
            Log.e(TAG, "Trying to insert empty list at position $position")
        }
    }

    /**
     * Inserts an array of [RecyclerItem]s at the specified position
     *
     * @param position The position to insert the items at
     * @param notifyAdapter Whether to notify the adapter about the change
     * @param items The items to insert
     */
    @JvmOverloads
    fun insertItems(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                    notifyAdapter: Boolean = true,
                    vararg items: RecyclerItem<*, *>) {

        insertItems(position, listOf(*items), notifyAdapter)
    }

    /**
     * Removes the [RecyclerItem] at the specified position
     *
     * @param position The position of the item to be removed
     * @param notifyAdapter Whether to notify the adapter about the change
     *
     * @return The removed [RecyclerItem] or null if not found
     */
    @JvmOverloads
    fun removeItem(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) position: Int,
                   notifyAdapter: Boolean = true): RecyclerItem<*, *>? {

        val removedItem = removeItemInternal(position)
        if (removedItem != null && notifyAdapter) {
            notifyItemRemoved(position)
        }

        return removedItem
    }

    /**
     * Removes a range of [RecyclerItem]s from the adapter
     *
     * @param startPosition The position of the first item to be removed
     * @param count The items count including the first to be removed
     * @param notifyAdapter Whether to notify the adapter about the change
     *
     * @return The list of removed items. If there were no removed items, the list will be empty
     */
    @JvmOverloads
    fun removeRange(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) startPosition: Int,
                    @IntRange(from = 1, to = Integer.MAX_VALUE.toLong()) count: Int,
                    notifyAdapter: Boolean = true): List<RecyclerItem<*, ViewHolder<*>>> {

        if (startPosition < 0 || startPosition + count > itemCount) {
            throw IndexOutOfBoundsException("Invalid deletion range [$startPosition, ${startPosition + count}). Adapter count is $itemCount")
        }

        val removedItems = ArrayList<RecyclerItem<*, ViewHolder<*>>>(count)
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
     * Removes all [RecyclerItem]s from the adapter
     *
     * @param notifyAdapter Whether to notify the adapter about the change
     *
     * @return The list of removed items. If there were no removed items, the list will be empty
     */
    @JvmOverloads
    fun removeAll(notifyAdapter: Boolean = true): List<RecyclerItem<*, ViewHolder<*>>> = removeRange(0, itemCount, notifyAdapter)

    /**
     * Moves the [RecyclerItem] at the `fromPosition` to the position, specified by `toPosition`
     *
     * @param fromPosition The current position of the [RecyclerItem]
     * @param toPosition The final position of the [RecyclerItem]
     * @param notifyAdapter Whether to notify the adapter about the change
     */
    @JvmOverloads
    fun moveItem(@IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) fromPosition: Int,
                 @IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) toPosition: Int,
                 notifyAdapter: Boolean = true) {

        val size = recyclerItems.size

        if (fromPosition in 0 until size && toPosition in 0 until size) {
            val itemToMove = recyclerItems.removeAt(fromPosition)
            recyclerItems.add(toPosition, itemToMove)
        } else {
            val error = "Moving item from $fromPosition to $toPosition failed! Items count was $size"
            Log.e(TAG, error)
        }

        if (notifyAdapter) {
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    /**
     * Finds the position of the first item with the specified type
     *
     * @param itemType The item type of the [RecyclerItem]
     *
     * @return The index of the first [RecyclerItem] with the specified type or -1 if not found
     */
    fun findFirstItemTypePosition(itemType: Int): Int = recyclerItems.indices.firstOrNull {
        getItem<RecyclerItem<*, *>>(it).type == itemType
    } ?: -1

    /**
     * Finds the position of the last item with the specified type
     *
     * @param itemType The item type of the [RecyclerItem]
     *
     * @return The index of the last [RecyclerItem] with the specified type or -1 if not found
     */
    fun findLastItemTypePosition(itemType: Int): Int = recyclerItems.indices.reversed().firstOrNull {
        getItem<RecyclerItem<*, *>>(it).type == itemType
    } ?: -1

    private fun insertItemInternal(position: Int, item: RecyclerItem<*, ViewHolder<*>>?) {
        if (position < 0 || position > itemCount) {
            throw IndexOutOfBoundsException("Invalid insertion position: $position. Adapter size is $itemCount")
        }

        if (item != null) {
            if (position == recyclerItems.size) {
                recyclerItems.add(item)
            } else {
                recyclerItems[position] = item
            }

            if (itemTypeItemMap.get(item.type) == null) {
                itemTypeItemMap.put(item.type, item)
            }
        } else {
            Log.e(TAG, "Trying to insert null item at position $position")
        }
    }

    private fun removeItemInternal(position: Int): RecyclerItem<*, ViewHolder<*>>? {

        val removed = recyclerItems.removeAt(position)

        // Clear cached category RecyclerItem, if this is the last item of the category
        if (findFirstItemTypePosition(removed.type) < 0) {
            itemTypeItemMap.remove(removed.type)
        }

        return removed
    }

    abstract class OnItemClickListener {

        /**
         * Called on itemView click event
         *
         * @param v The itemView of the [RecyclerItem]'s [ViewHolder]
         * @param position The position of the touched [RecyclerItem] in the adapter
         */
        abstract fun onItemClicked(v: View, position: Int)

        /**
         * Called on item touch event
         *
         * @param v The itemView of the [RecyclerItem]'s [ViewHolder]
         * @param event The [MotionEvent] on the itemView
         * @param position The position of the touched [RecyclerItem] in the adapter
         *
         * @return `true` if the event is handled, `false` if the event is unhandled
         *
         * **NOTE:** Returning `true` will stop triggering of subsequent gesture events like [View.OnClickListener.onClick]
         */
        open fun onItemTouched(v: View, event: MotionEvent, position: Int): Boolean = false
    }

    abstract class RecyclerItem<T, out VH : ViewHolder<T>> {

        /**
         * @return The stable ID for the item. If [hasStableIds] would return false this property should have [RecyclerView.NO_ID] value,
         * which is the default value
         *
         * @see RecyclerView.Adapter.getItemId
         */
        open val id: Long = RecyclerView.NO_ID

        /**
         * An unique [Int] which will be used as item type for the category(all items of the same type)
         *
         * **NOTE:** The value should be unique across all [RecyclerItem] categories in one instance of [DiverseRecyclerAdapter].
         * If you duplicate the item types the [RecyclerView] will show invalid data
         *
         * @see android.support.v7.widget.RecyclerView.Adapter.getItemViewType
         */
        @get:IntRange(from = 0, to = Integer.MAX_VALUE.toLong())
        open val type: Int = 0

        /**
         * @return An object, containing the data to be displayed in related [ViewHolder]. The same object will be passed in [ViewHolder.bindTo]
         */
        open val data: T? = null

        /**
         * You should create a new [ViewHolder] for the corresponding [RecyclerItem]
         *
         * @param parent The [ViewGroup] into which the [ViewHolder]'s itemView will be added after [ViewHolder] is bound to an adapter position
         *
         * @return A new [ViewHolder] that holds the [View] for the corresponding [RecyclerItem]
         *
         * @see android.support.v7.widget.RecyclerView.Adapter.onCreateViewHolder
         */
        protected abstract fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): VH

        internal fun createViewHolderInternal(parent: ViewGroup): VH = createViewHolder(parent, LayoutInflater.from(parent.context))
    }

    abstract class ViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * @return `true` if the [ViewHolder]'s itemView is attached to the parent [RecyclerView]
         * i.e. visible, `false` otherwise
         */
        var isAttached: Boolean = false
            private set

        /**
         * You should update the UI of the recycler item here.
         *
         * **NOTE:** itemView click listener is preserved for adapter internal usage. If you need itemView click
         * listener, you should override [ViewHolder.onItemViewClicked] or use a child view of the itemView as click handler
         *
         * @param data The data object holding the information for the [ViewHolder]. This object is the one returned by [RecyclerItem.data]
         *
         * @see RecyclerView.Adapter.onBindViewHolder
         */
        protected abstract fun bindTo(data: T?)

        internal fun bindToInternal(data: Any?) {
            @Suppress("UNCHECKED_CAST")
            bindTo(data as T?)
        }

        /**
         * Called when the itemView of this [ViewHolder] is attached to it's parent
         *
         * This can be used as a reasonable signal that the view is about to be seen by the user. If the [ViewHolder] previously
         * freed any resources in [ViewHolder.onDetachedFromWindow] those resources should be restored here.
         *
         * @see RecyclerView.Adapter.onViewAttachedToWindow
         */
        protected open fun onAttachedToWindow() {}

        internal fun onAttachedToWindowInternal() {
            isAttached = true

            onAttachedToWindow()
        }

        /**
         * Called when the itemView of this [ViewHolder] is detached from it's parent
         *
         * Becoming detached from the window is not necessarily a permanent condition. The consumer of an Adapter's views may
         * choose to cache views offscreen while they are not visible, attaching an detaching them as appropriate.
         *
         * @see RecyclerView.Adapter.onViewDetachedFromWindow
         */
        protected open fun onDetachedFromWindow() {}

        internal fun onDetachedFromWindowInternal() {
            isAttached = false

            onDetachedFromWindow()
        }

        /**
         * Called just before this [ViewHolder] instance is going to be recycled.
         *
         * A view is recycled when a [android.support.v7.widget.RecyclerView.LayoutManager] decides that it no longer
         * needs to be attached to it's parent [RecyclerView]. If an item view has large or expensive
         * data bound to it such as large bitmaps, this is a good place to release those resources.
         *
         * [RecyclerView] calls this method right before clearing [ViewHolder]'s internal data and sending it to
         * RecycledViewPool. This way, if ViewHolder was holding valid information before being recycled, you can call
         * [ViewHolder.getAdapterPosition] to get its adapter position.
         *
         * @see RecyclerView.Adapter.onViewRecycled
         */
        protected open fun unbind() {}

        internal fun unbindInternal() {
            unbind()
        }

        /**
         * Called when the itemView of the [ViewHolder] is clicked
         */
        protected open fun onItemViewClicked() {}

        internal fun onItemViewClickedInternal() {
            onItemViewClicked()
        }

        /**
         * @return the [View] for the given resource id. Tries to cast it to the inferred type
         */
        @CheckResult
        protected fun <V : View> findViewById(@IdRes id: Int): V? = itemView.findViewById(id) as V?
    }
}
