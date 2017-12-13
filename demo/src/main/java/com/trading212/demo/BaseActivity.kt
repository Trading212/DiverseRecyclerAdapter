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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val adapter = DiverseRecyclerAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = DiverseLinearLayoutManager(this)
        recyclerView.adapter = adapter

        fillElements(adapter)
    }

    abstract fun fillElements(adapter: DiverseRecyclerAdapter)
}
