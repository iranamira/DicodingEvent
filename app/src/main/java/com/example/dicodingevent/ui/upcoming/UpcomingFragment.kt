package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.adapters.UpcomingAdapter
import com.example.dicodingevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {
    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var upcomingAdapter: UpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        getUpcomingEvents()
        setupObservers()
    }

    private fun setupViewModel() {
        upcomingViewModel = ViewModelProvider(requireActivity())[UpcomingViewModel::class.java]
    }

    private fun setupRecyclerView() {
        upcomingAdapter = UpcomingAdapter(mutableListOf())
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcomingEvent.adapter = upcomingAdapter
    }

    private fun getUpcomingEvents() {
        upcomingViewModel.getUpcomingEvents()
    }

    private fun setupObservers() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            upcomingAdapter.updateData(it)
        }
    }
}