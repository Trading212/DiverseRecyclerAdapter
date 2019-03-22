package com.trading212.demo

import com.trading212.demo.item.ActivityChooserRecyclerItem
import com.trading212.demo.samples.*
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

class MainActivity : BaseActivity() {

    override fun fillElements(adapter: DiverseRecyclerAdapter) {

        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Homogeneous List", HomogeneousListActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Heterogeneous List", HeterogeneousListActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Payload updated List", PayloadUpdatesActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Single Selection List", SingleSelectionListActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Multiple Selection List", MultipleSelectionListActivity::class.java)), false)
        adapter.addItem(ActivityChooserRecyclerItem(ActivityChooserRecyclerItem.ActivityInfo("Drag&Drop", DragToReorderActivity::class.java)), false)

        adapter.notifyDataSetChanged()
    }
}
