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
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.adapter.FinishedAdapter
import com.example.dicodingevent.viewmodel.FinishedViewModel

class FinishedFragment : Fragment() {
    private lateinit var binding: FragmentFinishedBinding
    private lateinit var finishedViewModel: FinishedViewModel
    private lateinit var finishedAdapter: FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        getFinishedEvents()
        setupObservers()
    }

    private fun setupViewModel() {
        finishedViewModel = ViewModelProvider(requireActivity())[FinishedViewModel::class.java]
    }

    private fun setupRecyclerView() {
        val navController = findNavController()
        finishedAdapter = FinishedAdapter(listOf(), navController)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinishedEvent.adapter = finishedAdapter
    }

    private fun getFinishedEvents() {
        binding.rvFinishedEvent.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        finishedViewModel.getFinishedEvents()
    }

    private fun setupObservers() {
        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) {
            binding.rvFinishedEvent.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            finishedAdapter.updateData(it)
        }
        finishedViewModel.exception.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Internet tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
                finishedViewModel.resetExceptionValue()
            }
        }
    }
}