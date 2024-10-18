package com.example.dicodingevent.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.dicodingevent.R
import com.example.dicodingevent.adapter.MenuAdapter
import com.example.dicodingevent.data.local.LocalDatabase
import com.example.dicodingevent.data.remote.api.ApiClient
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.databinding.ActivitySearchBinding
import com.example.dicodingevent.viewmodel.SearchViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class SearchActivity : AppCompatActivity(R.layout.activity_search) {
    private val binding by viewBinding(ActivitySearchBinding::bind)
    private val searchViewModel by lazy {
        val eventRepository = EventRepository(
            ApiClient.apiClient,
            LocalDatabase.getDatabase(this).getEventDao()
        )
        val factory = ViewModelFactory(eventRepository)
        ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        setupRecyclerView()
        setupSearchView()
        setupObservers()
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        menuAdapter = MenuAdapter(listOf())
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(this)
        binding.rvUpcomingEvent.adapter = menuAdapter
    }

    private fun setupSearchView() {
        binding.searchView.queryHint = "Cari event yang bikin kamu tertarik"

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchViewModel.getEventsByKeyword(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupObservers() {
        searchViewModel.eventsByKeyword.observe(this) {
            menuAdapter.updateData(it)

            if (it.isNotEmpty()) {
                binding.rvUpcomingEvent.visibility = View.VISIBLE
                binding.tvNoResult.visibility = View.GONE
            } else {
                binding.rvUpcomingEvent.visibility = View.GONE
                binding.tvNoResult.text = resources.getString(R.string.no_result_keyword)
                binding.tvNoResult.visibility = View.VISIBLE
            }
        }

        searchViewModel.exception.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvNoResult.text = resources.getString(R.string.failed_to_load_data)
                binding.tvNoResult.visibility = View.VISIBLE
                searchViewModel.resetExceptionValue()
            }
        }

        searchViewModel.isLoading.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvUpcomingEvent.visibility = View.GONE
                binding.tvNoResult.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}