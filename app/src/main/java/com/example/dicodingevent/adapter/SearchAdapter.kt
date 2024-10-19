package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.databinding.ItemEventBinding
import com.example.dicodingevent.ui.activity.DetailActivity
import com.example.dicodingevent.util.DateTimeUtil.convertDate
import com.example.dicodingevent.util.EventUtil

class SearchAdapter(
    private var upcomingEventItems: List<Event>
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
    fun updateData(upcomingEvents: List<Event>) {
        upcomingEventItems = upcomingEvents
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        private val itemBinding: ItemEventBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Event) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.tvEventDate.text = convertDate(itemView.context, data.beginTime, data.endTime)
            itemBinding.tvEventCategory.text = data.category

            itemBinding.container.setOnClickListener {
                EventUtil.eventId = data.id
                itemBinding.root.context.startActivity(
                    Intent(itemBinding.root.context, DetailActivity::class.java)
                )
            }
        }
    }
}