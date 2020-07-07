package com.trading212.demo.item

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.trading212.demo.R
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

/**
 * Created by svetlin on 9.12.17.
 */
class ActivityChooserRecyclerItem(activityInfo: ActivityInfo) :
        DiverseRecyclerAdapter.RecyclerItem<ActivityChooserRecyclerItem.ActivityInfo, ActivityChooserRecyclerItem.ViewHolder>() {

    override val data: ActivityInfo = activityInfo

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): ViewHolder =
            ViewHolder(inflater.inflate(R.layout.item_simple_text, parent, false))

    data class ActivityInfo(val name: String, val clazz: Class<out Activity>)

    class ViewHolder(itemView: View) : DiverseRecyclerAdapter.ViewHolder<ActivityInfo>(itemView) {

        private var nameTextView = findViewById<TextView>(R.id.textView)

        override fun bindTo(data: ActivityInfo) {
            nameTextView.text = data.name
        }

        override fun onItemViewClicked() {
            super.onItemViewClicked()

            val lastData = lastData ?: return

            val intent = Intent(itemView.context, lastData.clazz)
            itemView.context.startActivity(intent)
        }
    }
}