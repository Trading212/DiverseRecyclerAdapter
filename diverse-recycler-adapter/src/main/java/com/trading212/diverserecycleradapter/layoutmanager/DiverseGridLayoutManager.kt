package com.trading212.diverserecycleradapter.layoutmanager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by svetlin.mollov on 12.12.2017 г..
 */
open class DiverseGridLayoutManager : GridLayoutManager {

    @JvmOverloads
    @Suppress("UNUSED")
    constructor(
            context: Context,
            spanCount: Int,
            orientation: Int = RecyclerView.VERTICAL,
            reverseLayout: Boolean = false
    ) : super(context, spanCount, orientation, reverseLayout)

    @Suppress("UNUSED")
    constructor(
            context: Context,
            attrs: AttributeSet,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow(view: RecyclerView?) {

        super.onAttachedToWindow(view)

        delegateRecyclerViewAttachedToWindow(view!!)
    }

    override fun onDetachedFromWindow(view: RecyclerView, recycler: RecyclerView.Recycler?) {

        delegateRecyclerViewDetachedFromWindow(view)

        super.onDetachedFromWindow(view, recycler)
    }
}