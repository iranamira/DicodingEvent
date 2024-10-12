package com.example.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.adapter.HomeFinishedAdapter
import com.example.dicodingevent.adapter.HomeUpcomingAdapter
import com.example.dicodingevent.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeUpcomingAdapter: HomeUpcomingAdapter
    private lateinit var homeFinishedAdapter: HomeFinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        homeUpcomingAdapter = HomeUpcomingAdapter(listOf())
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvUpcomingEvent.adapter = homeUpcomingAdapter

        homeFinishedAdapter = HomeFinishedAdapter(listOf())
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinishedEvent.adapter = homeFinishedAdapter
    }

    private fun getUpcomingEvents() {
        binding.tvUpcomingText.visibility = View.GONE
        binding.rvUpcomingEvent.visibility = View.GONE
        binding.tvFinishedText.visibility = View.GONE
        binding.rvFinishedEvent.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        homeViewModel.getUpcomingEvents()
        homeViewModel.getFinishedEvents()
    }

    private fun setupObservers() {
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            binding.tvUpcomingText.visibility = View.VISIBLE
            binding.rvUpcomingEvent.visibility = View.VISIBLE
            binding.tvFinishedText.visibility = View.VISIBLE
            binding.rvFinishedEvent.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            homeUpcomingAdapter.updateData(it)
        }
        homeViewModel.finishedEvents.observe(viewLifecycleOwner) {
            homeFinishedAdapter.updateData(it)
        }
        homeViewModel.exception.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Internet tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
                homeViewModel.resetExceptionValue()
            }
        }
    }
}