package com.trading212.demo.samples

import com.trading212.demo.BaseActivity
import com.trading212.demo.item.SimpleTextRecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.util.onItemClicked

class PayloadUpdatesActivity : BaseActivity() {

    private var counter = 0

    override fun fillElements(adapter: DiverseRecyclerAdapter) {

        for (i in 0..49) {
            adapter.addItem(SimpleTextRecyclerItem("Item $i"), false)
        }

        adapter.notifyDataSetChanged()

        adapter.onItemClicked { _, position ->
            adapter.notifyItemChanged(position, SimplePayload("${++counter}"))
        }
    }

    class SimplePayload(val message: String)
}