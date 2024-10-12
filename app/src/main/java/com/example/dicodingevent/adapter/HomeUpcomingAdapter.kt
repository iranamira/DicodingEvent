package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.model.ListEvents
import com.example.dicodingevent.databinding.ItemEventHomeHorizontalBinding

class HomeUpcomingAdapter(
    private var upcomingEventItems: List<ListEvents>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventHomeHorizontalBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = upcomingEventItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(upcomingEventItems[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(upcomingEvents: List<ListEvents>) {
        upcomingEventItems = upcomingEvents
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        private val itemBinding: ItemEventHomeHorizontalBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ListEvents) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
        }
    }
}