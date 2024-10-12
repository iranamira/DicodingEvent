package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.dicodingevent.data.model.ListEvents
import com.example.dicodingevent.databinding.ItemEventHomeVerticalBinding

class HomeFinishedAdapter(
    private var finishedEventItems: List<ListEvents>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventHomeVerticalBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = finishedEventItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(finishedEventItems[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(finishedEvents: List<ListEvents>) {
        finishedEventItems = finishedEvents
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        private val itemBinding: ItemEventHomeVerticalBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ListEvents) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .transform(CircleCrop())
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
        }
    }
}