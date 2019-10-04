package com.trading212.demo.item

import androidx.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.trading212.demo.R
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

/**
 * Created by svetlin on 9.12.17.
 */
class ImageTextRecyclerItem(imageData: ImageData) :
        DiverseRecyclerAdapter.RecyclerItem<ImageTextRecyclerItem.ImageData, ImageTextRecyclerItem.ViewHolder>() {

    companion object {

        @JvmField
        val TYPE = ItemType.IMAGE_TEXT.ordinal
    }

    override val type: Int = TYPE

    override val data: ImageData? = imageData

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): ViewHolder =
            ViewHolder(inflater.inflate(R.layout.item_image_text, parent, false))

    data class ImageData(val name: String, @DrawableRes val resId: Int)

    class ViewHolder(itemView: View) : DiverseRecyclerAdapter.ViewHolder<ImageData>(itemView) {

        private val textView = findViewById<TextView>(R.id.textView)

        private val imageView = findViewById<ImageView>(R.id.imageView)

        override fun bindTo(data: ImageData?) {

            textView?.text = data?.name
            imageView?.setImageResource(data!!.resId)
        }
    }
}