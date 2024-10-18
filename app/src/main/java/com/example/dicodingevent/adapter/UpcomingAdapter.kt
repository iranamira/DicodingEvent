package com.example.dicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.databinding.ItemEventBinding
import com.example.dicodingevent.util.DateTimeUtil
import com.example.dicodingevent.util.EventUtil

class UpcomingAdapter(
    private var upcomingEventItems: List<Event>,
    private val navController: NavController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
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
        private val itemBinding: ItemEventBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Event, navController: NavController) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.tvEventDate.text = convertDate(itemBinding, data.beginTime, data.endTime)
            itemBinding.tvEventCategory.text = data.category

            itemBinding.container.setOnClickListener {
                EventUtil.eventId = data.id
                navController.navigate(R.id.action_fragmentUpcoming_to_detailActivity)
            }
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