package com.trading212.demo.samples

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.trading212.demo.R
import com.trading212.demo.item.SimpleTextRecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.layoutmanager.DiverseLinearLayoutManager
import com.trading212.diverserecycleradapter.util.replaceItems

class ReplaceItemsActivity : AppCompatActivity() {

    private val adapter = DiverseRecyclerAdapter()

    private var start = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_replace_items)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = DiverseLinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    fun replaceItems(view: View) {

        val items = (start..start + 9).map { SimpleTextRecyclerItem("Item $it") }

        adapter.replaceItems(items)

        start += 5
    }
}