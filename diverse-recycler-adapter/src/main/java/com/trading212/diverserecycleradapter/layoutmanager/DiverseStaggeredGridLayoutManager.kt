package com.trading212.diverserecycleradapter.layoutmanager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet

/**
 * Created by svetlin.mollov on 12.12.2017 г..
 */
open class DiverseStaggeredGridLayoutManager : StaggeredGridLayoutManager {

    @JvmOverloads
    @Suppress("UNUSED")
    constructor(spanCount: Int, orientation: Int = LinearLayoutManager.VERTICAL) : super(spanCount, orientation)

    @Suppress("UNUSED")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow(view: RecyclerView?) {

        super.onAttachedToWindow(view)

        delegateRecyclerViewAttachedToWindow(view!!)
    }

    override fun onDetachedFromWindow(view: RecyclerView, recycler: RecyclerView.Recycler?) {

        delegateRecyclerViewDetachedFromWindow(view)

        super.onDetachedFromWindow(view, recycler)
    }
}