package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.databinding.ItemEventHomeHorizontalBinding
import com.example.dicodingevent.util.EventUtil

class HomeUpcomingAdapter(
    private var upcomingEventItems: List<Event>,
    private val navController: NavController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventHomeHorizontalBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = upcomingEventItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(upcomingEventItems[position], navController)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(upcomingEvents: List<Event>) {
        upcomingEventItems = upcomingEvents
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        private val itemBinding: ItemEventHomeHorizontalBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Event, navController: NavController) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.container.setOnClickListener {
                EventUtil.eventId = data.id
                navController.navigate(R.id.action_fragmentHome_to_detailActivity)
            }
        }
    }
}