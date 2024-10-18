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
import com.example.dicodingevent.adapter.UpcomingAdapter
import com.example.dicodingevent.data.local.LocalDatabase
import com.example.dicodingevent.data.remote.api.ApiClient
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.viewmodel.UpcomingViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {
    private val binding by viewBinding(FragmentUpcomingBinding::bind)
    private val upcomingViewModel by lazy {
        val eventRepository = EventRepository(
            ApiClient.apiClient,
            LocalDatabase.getDatabase(requireActivity()).getEventDao()
        )
        val factory = ViewModelFactory(eventRepository)
        ViewModelProvider(requireActivity(), factory)[UpcomingViewModel::class.java]
    }
    private lateinit var upcomingAdapter: UpcomingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getUpcomingEvents()
        setupSwipeRefresh()
        setupObservers()
    }

    private fun setupRecyclerView() {
        val navController = findNavController()
        upcomingAdapter = UpcomingAdapter(listOf(), navController)
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcomingEvent.adapter = upcomingAdapter
    }

    private fun getUpcomingEvents() {
        upcomingViewModel.getUpcomingEvents()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            upcomingViewModel.refreshUpcomingEvents()
        }
    }

    private fun setupObservers() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            binding.tvFailedLoadData.visibility = View.GONE
            binding.rvUpcomingEvent.visibility = View.VISIBLE
            upcomingAdapter.updateData(it)
        }

        upcomingViewModel.exception.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvFailedLoadData.visibility = View.VISIBLE
                upcomingViewModel.resetExceptionValue()
            }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.rvUpcomingEvent.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        upcomingViewModel.isRefreshLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
    }
}