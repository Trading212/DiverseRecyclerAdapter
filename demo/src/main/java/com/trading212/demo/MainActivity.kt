package com.trading212.demo

import com.trading212.demo.item.ActivityChooserRecyclerItem
import com.trading212.demo.samples.HeterogeneousActivity
import com.trading212.demo.samples.HomogeneousListActivity
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

class MainActivity : BaseActivity() {

    override fun fillElements(adapter: DiverseRecyclerAdapter) {

        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Homogeneous List", HomogeneousListActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Heterogeneous List", HeterogeneousActivity::class.java)), false)

        adapter.notifyDataSetChanged()
    }
}
