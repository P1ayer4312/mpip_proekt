package com.example.proekt_mpip_181190.adapters

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proekt_mpip_181190.R
import com.example.proekt_mpip_181190.models.RecipeCardData

class FeedRecyclerViewAdapter(
    private val data: List<RecipeCardData>,
    private val listener: RecyclerViewEvent
) : RecyclerView.Adapter<FeedRecyclerViewAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val thumbnail: ImageView = view.findViewById(R.id.feedCard_thumbnail)
        val title: TextView = view.findViewById(R.id.feedCard_title)
        val author: TextView = view.findViewById(R.id.feedCard_author)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_recycler_view_row, parent, false)

        return ItemViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val recipeCardData: RecipeCardData = data[position]

        holder.title.text = recipeCardData.title
        holder.author.text = recipeCardData.author
        Glide.with(holder.itemView)
            .load(recipeCardData.imageLink)
            .into(holder.thumbnail)
    }

    interface RecyclerViewEvent {
        fun onItemClick(position: Int)
    }
}































