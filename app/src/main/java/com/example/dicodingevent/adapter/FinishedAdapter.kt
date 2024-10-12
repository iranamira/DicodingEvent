package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.model.ListEvents
import com.example.dicodingevent.databinding.ItemEventBinding
import com.example.dicodingevent.util.DateTimeUtil

class FinishedAdapter(
    private var finishedEventItems: List<ListEvents>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
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
        private val itemBinding: ItemEventBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ListEvents) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.tvEventTime.text = convertDate(itemBinding, data.beginTime, data.endTime)
            itemBinding.tvEventCategory.text = data.category
        }

        private fun convertDate(itemBinding: ItemEventBinding, begin: String, end: String): String {
            val beginDate = begin.substring(0, 10)
            val beginTime = begin.substring(11)
            val endDate = end.substring(0, 10)
            val endTime = end.substring(11)

            val beginFinal = DateTimeUtil.getIndonesianDateFormat(beginDate)
            val endFinal = DateTimeUtil.getIndonesianDateFormat(endDate)

            return if (beginFinal == endFinal) {
                itemBinding.root.context.resources.getString(
                    R.string.event_date_same_day,
                    beginFinal,
                    beginTime,
                    endTime
                )
            } else {
                itemBinding.root.context.resources.getString(
                    R.string.event_date_different_day,
                    beginFinal,
                    beginTime,
                    endFinal,
                    endTime
                )
            }
        }
    }
}