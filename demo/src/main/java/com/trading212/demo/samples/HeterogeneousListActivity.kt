package com.trading212.demo.samples

import android.widget.Toast
import com.trading212.demo.BaseActivity
import com.trading212.demo.R
import com.trading212.demo.item.ImageTextRecyclerItem
import com.trading212.demo.item.SimpleImageRecyclerItem
import com.trading212.demo.item.SimpleTextRecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.util.onItemClicked
import com.trading212.diverserecycleradapter.util.onItemLongClicked

/**
 * Created by svetlin on 9.12.17.
 */
class HeterogeneousListActivity : BaseActivity() {

    override fun fillElements(adapter: DiverseRecyclerAdapter) {

        val items = ArrayList<DiverseRecyclerAdapter.RecyclerItem<*, DiverseRecyclerAdapter.ViewHolder<*>>>()

        for (i in 1..30) {
            when {
                i % 3 == 0 -> items.add(SimpleImageRecyclerItem())
                i % 2 == 0 -> items.add(ImageTextRecyclerItem(ImageTextRecyclerItem.ImageData("Christmas Time", R.drawable.christmas)))
                else -> items.add(SimpleTextRecyclerItem("Text item $i"))
            }
        }

        adapter.addItems(items)

        adapter.onItemClicked { v, position ->
            val text = when (adapter.getItemViewType(position)) {

                SimpleImageRecyclerItem.TYPE -> "Clicked image at position $position"
                SimpleTextRecyclerItem.TYPE -> "Clicked ${adapter.getItem<SimpleTextRecyclerItem>(position).data}"
                ImageTextRecyclerItem.TYPE -> "Clicked  image ${adapter.getItem<ImageTextRecyclerItem>(position).data.name}"
                else -> "Unknown item clicked"
            }

            Toast.makeText(v.context, text, Toast.LENGTH_SHORT).show()
        }

        adapter.onItemLongClicked { v, position ->
            val text = when (adapter.getItemViewType(position)) {

                SimpleImageRecyclerItem.TYPE -> "Long clicked image at position $position"
                SimpleTextRecyclerItem.TYPE -> "Long clicked ${adapter.getItem<SimpleTextRecyclerItem>(position).data}"
                ImageTextRecyclerItem.TYPE -> "Long clicked  image ${adapter.getItem<ImageTextRecyclerItem>(position).data.name}"
                else -> "Unknown item long clicked"
            }

            Toast.makeText(v.context, text, Toast.LENGTH_SHORT).show()

            false
        }
    }
}