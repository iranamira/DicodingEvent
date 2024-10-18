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
import com.example.dicodingevent.adapter.FinishedAdapter
import com.example.dicodingevent.data.local.LocalDatabase
import com.example.dicodingevent.data.remote.api.ApiClient
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.viewmodel.FinishedViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class FinishedFragment : Fragment(R.layout.fragment_finished) {
    private val binding by viewBinding(FragmentFinishedBinding::bind)
    private val finishedViewModel by lazy {
        val eventRepository = EventRepository(
            ApiClient.apiClient,
            LocalDatabase.getDatabase(requireActivity()).getEventDao()
        )
        val factory = ViewModelFactory(eventRepository)
        ViewModelProvider(requireActivity(), factory)[FinishedViewModel::class.java]
    }
    private lateinit var finishedAdapter: FinishedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getFinishedEvents()
        setupSwipeRefresh()
        setupObservers()
    }

    private fun setupRecyclerView() {
        val navController = findNavController()
        finishedAdapter = FinishedAdapter(listOf(), navController)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinishedEvent.adapter = finishedAdapter
    }

    private fun getFinishedEvents() {
        finishedViewModel.getFinishedEvents()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            finishedViewModel.refreshFinishedEvents()
        }
    }

    private fun setupObservers() {
        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) {
            binding.tvFailedLoadData.visibility = View.GONE
            binding.rvFinishedEvent.visibility = View.VISIBLE
            finishedAdapter.updateData(it)
        }

        finishedViewModel.exception.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvFailedLoadData.visibility = View.VISIBLE
                finishedViewModel.resetExceptionValue()
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.rvFinishedEvent.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        finishedViewModel.isRefreshLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
    }
}