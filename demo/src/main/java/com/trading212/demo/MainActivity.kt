package com.trading212.demo

import com.trading212.demo.item.ActivityChooserRecyclerItem
import com.trading212.demo.samples.HeterogeneousListActivity
import com.trading212.demo.samples.HomogeneousListActivity
import com.trading212.demo.samples.SingleChoiceListActivity
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

class MainActivity : BaseActivity() {

    override fun fillElements(adapter: DiverseRecyclerAdapter) {

        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Homogeneous List", HomogeneousListActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Heterogeneous List", HeterogeneousListActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Single Choice List", SingleChoiceListActivity::class.java)), false)

        adapter.notifyDataSetChanged()
    }
}
