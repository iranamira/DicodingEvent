package com.example.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.dicodingevent.R
import com.example.dicodingevent.adapter.HomeFinishedAdapter
import com.example.dicodingevent.adapter.HomeUpcomingAdapter
import com.example.dicodingevent.data.local.LocalDatabase
import com.example.dicodingevent.data.remote.api.ApiClient
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.viewmodel.HomeViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel by lazy {
        val eventRepository = EventRepository(
            ApiClient.apiClient,
            LocalDatabase.getDatabase(requireActivity()).getEventDao()
        )
        val factory = ViewModelFactory(eventRepository)
        ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
    }
    private lateinit var homeUpcomingAdapter: HomeUpcomingAdapter
    private lateinit var homeFinishedAdapter: HomeFinishedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getUpcomingEvents()
        setupSwipeRefresh()
        setupObservers()
    }

    private fun setupRecyclerView() {
        val navController = findNavController()

        homeUpcomingAdapter = HomeUpcomingAdapter(listOf(), navController)
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvUpcomingEvent.adapter = homeUpcomingAdapter

        homeFinishedAdapter = HomeFinishedAdapter(listOf(), navController)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinishedEvent.adapter = homeFinishedAdapter
    }

    private fun getUpcomingEvents() {
        homeViewModel.getUpcomingAndFinishedEvents()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.refreshUpcomingAndFinishedEvents()
        }
    }

    private fun setupObservers() {
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            binding.apply {
                tvFailedLoadData.visibility = View.GONE
                tvUpcomingText.visibility = View.VISIBLE
                tvFinishedText.visibility = View.VISIBLE
                rvUpcomingEvent.visibility = View.VISIBLE
                rvFinishedEvent.visibility = View.VISIBLE
            }
            homeUpcomingAdapter.updateData(it)
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) {
            homeFinishedAdapter.updateData(it)
        }

        homeViewModel.exception.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvFailedLoadData.visibility = View.VISIBLE
                homeViewModel.resetExceptionValue()
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.apply {
                    tvUpcomingText.visibility = View.GONE
                    tvFinishedText.visibility = View.GONE
                    rvUpcomingEvent.visibility = View.GONE
                    rvFinishedEvent.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        homeViewModel.isRefreshLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
    }
}