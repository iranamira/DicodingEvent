package com.example.dicodingevent.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.adapter.MenuAdapter
import com.example.dicodingevent.databinding.ActivityMenuBinding
import com.example.dicodingevent.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupViewModel()
        setupButtonAndMenu()
        setupRecyclerView()
        setupSearchView()
        setupObservers()
    }

    private fun setupBinding() {
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    private fun setupButtonAndMenu() {
        binding.ibBack.setOnClickListener {
            finish()
        }
        binding.rvUpcomingEvent.visibility = View.GONE
        binding.tvNoResult.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
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
                    searchViewModel.getAllEventsByKeyword(query)
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvUpcomingEvent.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupObservers() {
        searchViewModel.allEventsByKeyword.observe(this) {
            binding.progressBar.visibility = View.GONE
            menuAdapter.updateData(it)

            if (it.isNotEmpty()) {
                binding.rvUpcomingEvent.visibility = View.VISIBLE
                binding.tvNoResult.visibility = View.GONE
            } else {
                binding.rvUpcomingEvent.visibility = View.GONE
                binding.tvNoResult.visibility = View.VISIBLE
            }
        }
        searchViewModel.exception.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "Internet tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
                searchViewModel.resetExceptionValue()
            }
        }
    }
}