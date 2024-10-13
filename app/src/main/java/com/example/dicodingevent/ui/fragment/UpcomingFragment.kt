package com.example.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.adapter.UpcomingAdapter
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.viewmodel.UpcomingViewModel

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
        val navController = findNavController()
        upcomingAdapter = UpcomingAdapter(listOf(), navController)
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcomingEvent.adapter = upcomingAdapter
    }

    private fun getUpcomingEvents() {
        binding.rvUpcomingEvent.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        upcomingViewModel.getUpcomingEvents()
    }

    private fun setupObservers() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            binding.rvUpcomingEvent.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            upcomingAdapter.updateData(it)
        }
        upcomingViewModel.exception.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Internet tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
                upcomingViewModel.resetExceptionValue()
            }
        }
    }
}