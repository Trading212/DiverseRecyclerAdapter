package com.trading212.demo.samples

import android.view.View
import android.widget.Toast
import com.trading212.demo.BaseActivity
import com.trading212.demo.R
import com.trading212.demo.item.ImageTextRecyclerItem
import com.trading212.demo.item.SimpleImageRecyclerItem
import com.trading212.demo.item.SimpleTextRecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

/**
 * Created by svetlin on 9.12.17.
 */
class HeterogeneousActivity : BaseActivity() {

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

        adapter.onItemClickListener = object : DiverseRecyclerAdapter.OnItemClickListener() {

            override fun onItemClicked(v: View, position: Int) {

                val text = when (adapter.getItemViewType(position)) {

                    SimpleImageRecyclerItem.TYPE -> "Clicked image at position $position"
                    SimpleTextRecyclerItem.TYPE -> "Clicked ${adapter.getItem<SimpleTextRecyclerItem>(position).data}"
                    ImageTextRecyclerItem.TYPE -> "Clicked  image ${adapter.getItem<ImageTextRecyclerItem>(position).data?.name}"
                    else -> "Unknown item clicked"
                }

                Toast.makeText(v.context, text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}