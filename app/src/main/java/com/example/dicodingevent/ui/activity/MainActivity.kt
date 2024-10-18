package com.example.dicodingevent.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        setupBottomNavigationView()
    }

    private fun setupButton() {
        binding.ibSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun setupBottomNavigationView() {
        val navView: BottomNavigationView = binding.navBottomView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentHome -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_home)
                    binding.ibSearch.visibility = View.VISIBLE
                }
                R.id.fragmentUpcoming -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_upcoming)
                    binding.ibSearch.visibility = View.GONE
                }
                R.id.fragmentFinished -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_finished)
                    binding.ibSearch.visibility = View.GONE
                }
                R.id.fragmentFavorite -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_favorite)
                    binding.ibSearch.visibility = View.GONE
                }
                R.id.fragmentSettings -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_settings)
                    binding.ibSearch.visibility = View.GONE
                }
            }
        }
    }
}