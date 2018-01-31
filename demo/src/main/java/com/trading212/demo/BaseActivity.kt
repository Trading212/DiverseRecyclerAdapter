package com.trading212.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.layoutmanager.DiverseLinearLayoutManager

/**
 * Created by svetlin on 9.12.17.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val adapter: DiverseRecyclerAdapter = DiverseRecyclerAdapter()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = DiverseLinearLayoutManager(this)
        recyclerView.adapter = adapter

        if (hasItemDecoration()) {
            recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        }

        fillElements(adapter)
    }

    fun getAdapter() = adapter

    fun getRecyclerView() = recyclerView

    open fun hasItemDecoration() = true

    abstract fun fillElements(adapter: DiverseRecyclerAdapter)
}
