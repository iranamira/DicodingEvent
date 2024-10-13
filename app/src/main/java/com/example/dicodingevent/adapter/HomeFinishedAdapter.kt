package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.dicodingevent.R
import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.databinding.ItemEventHomeVerticalBinding
import com.example.dicodingevent.util.EventUtil

class HomeFinishedAdapter(
    private var finishedEventItems: List<Event>,
    private val navController: NavController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventHomeVerticalBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = finishedEventItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(finishedEventItems[position], navController)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(finishedEvents: List<Event>) {
        finishedEventItems = finishedEvents
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        private val itemBinding: ItemEventHomeVerticalBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Event, navController: NavController) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .transform(CircleCrop())
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.container.setOnClickListener {
                EventUtil.eventId = data.id
                navController.navigate(R.id.action_fragmentHome_to_detailActivity)
            }
        }
    }
}