package com.example.dicodingevent.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupBottomNavigationView()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupBottomNavigationView() {
        val navView: BottomNavigationView = binding.navBottomView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> binding.tvToolbarTitle.text = resources.getString(
                    R.string.label_home
                )
                R.id.navigation_upcoming -> binding.tvToolbarTitle.text = resources.getString(
                    R.string.label_upcoming
                )
                R.id.navigation_finished -> binding.tvToolbarTitle.text = resources.getString(
                    R.string.label_finished
                )
            }
        }
    }
}