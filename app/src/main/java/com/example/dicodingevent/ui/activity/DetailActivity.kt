package com.example.dicodingevent.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.example.dicodingevent.util.DateTimeUtil
import com.example.dicodingevent.util.EventUtil
import com.example.dicodingevent.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupViewModel()
        setupButton()
        getEvent()
        setupObservers()
    }

    private fun setupBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
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
    }

    private fun getEvent() {
        binding.contentContainer.visibility = View.GONE
        binding.cvButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        detailViewModel.getEvent(EventUtil.eventId)
    }

    private fun setupObservers() {
        detailViewModel.event.observe(this) {
            binding.contentContainer.visibility = View.VISIBLE
            binding.cvButton.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE

            Glide.with(this)
                .load(it.imageLogo)
                .centerCrop()
                .into(binding.ivEventImage)

            binding.tvEventTitle.text = it.name
            binding.tvEventOwnerName.text = it.ownerName
            binding.tvEventCategoryName.text = it.category
            binding.tvEventDateBegin.text = convertDate(it.beginTime, it.endTime)
            binding.tvEventCityName.text = it.cityName
            binding.tvEventQuotaRemaining.text = (it.quota - it.registrants).toString()

            val htmlText = it.description
            binding.tvEventDescriptionText.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)

            url = it.link
        }
        detailViewModel.exception.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "Internet tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
                detailViewModel.resetExceptionValue()
            }
        }
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