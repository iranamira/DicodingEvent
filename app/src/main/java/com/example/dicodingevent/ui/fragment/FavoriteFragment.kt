package com.example.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.dicodingevent.R
import com.example.dicodingevent.adapter.FavoriteAdapter
import com.example.dicodingevent.data.local.LocalDatabase
import com.example.dicodingevent.data.remote.api.ApiClient
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.viewmodel.FavoriteViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val favoriteViewModel by lazy {
        val eventRepository = EventRepository(
            ApiClient.apiClient,
            LocalDatabase.getDatabase(requireActivity()).getEventDao()
        )
        val factory = ViewModelFactory(eventRepository)
        ViewModelProvider(requireActivity(), factory)[FavoriteViewModel::class.java]
    }
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getFavoriteEvents()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        getFavoriteEvents()
    }

    private fun setupRecyclerView() {
        val navController = findNavController()
        favoriteAdapter = FavoriteAdapter(listOf(), navController)
        binding.rvFavoriteEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFavoriteEvent.adapter = favoriteAdapter
    }

    private fun getFavoriteEvents() {
        favoriteViewModel.getFavoriteEvents()
    }

    private fun setupObservers() {
        favoriteViewModel.finishedEvents.observe(viewLifecycleOwner) {
            favoriteAdapter.updateData(it)

            if (it.isEmpty()) {
                binding.tvNoFavoriteEvent.visibility = View.VISIBLE
            } else {
                binding.tvNoFavoriteEvent.visibility = View.GONE
            }
        }
    }
}