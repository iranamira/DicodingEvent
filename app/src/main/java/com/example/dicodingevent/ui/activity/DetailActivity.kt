package com.example.dicodingevent.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.LocalDatabase
import com.example.dicodingevent.data.local.entity.Event
import com.example.dicodingevent.data.remote.api.ApiClient
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.example.dicodingevent.util.DateTimeUtil
import com.example.dicodingevent.util.EventUtil
import com.example.dicodingevent.viewmodel.DetailViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {
    private val binding by viewBinding(ActivityDetailBinding::bind)
    private val detailViewModel by lazy {
        val eventRepository = EventRepository(
            ApiClient.apiClient,
            LocalDatabase.getDatabase(this).getEventDao())
        val factory = ViewModelFactory(eventRepository)
        ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }
    private var url = ""
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        getEvent()
        checkIsEventExistInFavorite()
        setupObservers()
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            finish()
        }
        binding.registerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.ibFavorite.setOnClickListener {
            if (!isFavorite) {
                binding.ibFavorite.setImageResource(R.drawable.favorite)
                saveEventToFavorite()
            } else {
                binding.ibFavorite.setImageResource(R.drawable.unfavorite)
                removeEventFromFavorite(EventUtil.eventId)
            }
            isFavorite = !isFavorite
        }
    }

    private fun getEvent() {
        detailViewModel.getEvent(EventUtil.eventId)
    }

    private fun checkIsEventExistInFavorite() {
        detailViewModel.checkIsEventExistInFavorite(EventUtil.eventId)
    }

    private fun setupObservers() {
        detailViewModel.event.observe(this) {
            Glide.with(this)
                .load(it.imageLogo)
                .centerCrop()
                .into(binding.ivEventImage)
            url = it.link
            val htmlText = it.description

            binding.apply {
                tvEventTitle.text = it.name
                tvEventOwnerName.text = it.ownerName
                tvEventCategoryName.text = it.category
                tvEventDateBegin.text = convertDate(it.beginTime, it.endTime)
                tvEventCityName.text = it.cityName
                tvEventQuotaRemaining.text = (it.quota - it.registrants).toString()
                tvEventDescriptionText.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
                contentContainer.visibility = View.VISIBLE
                cvButton.visibility = View.VISIBLE
                tvFailedLoadData.visibility = View.GONE
            }
            EventUtil.eventDetail = it
        }

        detailViewModel.exception.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvFailedLoadData.visibility = View.VISIBLE
                detailViewModel.resetExceptionValue()
            }
        }

        detailViewModel.isLoading.observe(this) {
            if (it) {
                binding.contentContainer.visibility = View.GONE
                binding.cvButton.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        detailViewModel.isEventExistInFavorite.observe(this) {
            isFavorite = it
            if (isFavorite) {
                binding.ibFavorite.setImageResource(R.drawable.favorite)
            } else {
                binding.ibFavorite.setImageResource(R.drawable.unfavorite)
            }
        }
    }

    private fun saveEventToFavorite() {
        if (EventUtil.eventDetail != null) {
            detailViewModel.saveEventToFavorite(
                Event(
                    EventUtil.eventDetail!!.id,
                    EventUtil.eventDetail!!.name,
                    EventUtil.eventDetail!!.imageLogo,
                    EventUtil.eventDetail!!.category,
                    EventUtil.eventDetail!!.beginTime,
                    EventUtil.eventDetail!!.endTime
                )
            )
        }
        Toast.makeText(
            this,
            resources.getString(R.string.success_save_to_favorite),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun removeEventFromFavorite(id: Int) {
        detailViewModel.removeEventFromFavorite(id)
        Toast.makeText(
            this,
            resources.getString(R.string.success_remove_from_favorite),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun convertDate(begin: String, end: String): String {
        val beginDate = begin.substring(0, 10)
        val beginTime = begin.substring(11)
        val endDate = end.substring(0, 10)
        val endTime = end.substring(11)

        val beginFinal = DateTimeUtil.getIndonesianDateFormat(beginDate)
        val endFinal = DateTimeUtil.getIndonesianDateFormat(endDate)

        return if (beginFinal == endFinal) {
            resources.getString(
                R.string.event_date_same_day,
                beginFinal,
                beginTime,
                endTime
            )
        } else {
            resources.getString(
                R.string.event_date_different_day,
                beginFinal,
                beginTime,
                endFinal,
                endTime
            )
        }
    }
}