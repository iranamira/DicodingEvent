package com.example.dicodingevent.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.databinding.ItemEventBinding
import com.example.dicodingevent.models.ListEvents
import com.example.dicodingevent.models.UpcomingEvent

class UpcomingAdapter(
    private var upcomingEventItems: List<ListEvents>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
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

    inner class ItemViewHolder(
        private val itemBinding: ItemEventBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ListEvents) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.tvEventTime.text = "${data.beginTime} - ${data.endTime}"
            itemBinding.tvEventCategory.text = data.category
        }
    }
}