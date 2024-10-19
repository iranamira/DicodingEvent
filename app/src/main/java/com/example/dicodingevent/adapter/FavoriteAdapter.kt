package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.Event
import com.example.dicodingevent.databinding.ItemEventBinding
import com.example.dicodingevent.util.DateTimeUtil.convertDate
import com.example.dicodingevent.util.EventUtil

class FavoriteAdapter(
    private var finishedEventItems: List<Event>,
    private val navController: NavController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
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
        private val itemBinding: ItemEventBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Event, navController: NavController) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.tvEventDate.text = convertDate(itemView.context, data.beginTime, data.endTime)
            itemBinding.tvEventCategory.text = data.category

            itemBinding.container.setOnClickListener {
                EventUtil.eventId = data.id
                navController.navigate(R.id.action_fragmentFavorite_to_detailActivity)
            }
        }
    }
}