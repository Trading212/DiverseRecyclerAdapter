package com.trading212.demo.samples

import android.view.View
import com.trading212.demo.BaseActivity
import com.trading212.demo.item.SimpleTextRecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

class PayloadUpdatesActivity : BaseActivity() {

    private var payload = SimplePayload(0)

    override fun fillElements(adapter: DiverseRecyclerAdapter) {

        for (i in 0..49) {
            adapter.addItem(SimpleTextRecyclerItem("Item $i"), false)
        }

        adapter.notifyDataSetChanged()

        adapter.onItemActionListener = object : DiverseRecyclerAdapter.OnItemActionListener() {

            override fun onItemClicked(v: View, position: Int) {

                payload = payload.increment()

                adapter.notifyItemChanged(position, payload)
            }
        }
    }

    class SimplePayload(val counter: Int) {
        fun increment() = SimplePayload(counter + 1)
    }
}