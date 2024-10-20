package com.example.dicodingevent.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.datastore.DataStoreInstance
import com.example.dicodingevent.data.local.datastore.SettingsPreference
import com.example.dicodingevent.databinding.ActivityMainBinding
import com.example.dicodingevent.util.workmanager.DailyReminderWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val settingsPreference by lazy {
        SettingsPreference(DataStoreInstance.getInstance(this))
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                lifecycleScope.launch {
                    settingsPreference.updateNotification(true)
                }
            } else {
                lifecycleScope.launch {
                    settingsPreference.updateNotification(false)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        setupBottomNavigationView()
        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        setupPermission()
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

    private fun setupPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun setupDailyReminder() {
        val workName = "DAILY_REMINDER_WORK"
        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun cancelDailyReminder() {
        val workName = "DAILY_REMINDER_WORK"
        WorkManager.getInstance(this).cancelUniqueWork(workName)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            launch {
                settingsPreference.darkMode.collect { isEnabled ->
                    if (isEnabled) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
            launch {
                settingsPreference.notification.collect { isEnabled ->
                    if (isEnabled) {
                        setupDailyReminder()
                    } else {
                        cancelDailyReminder()
                    }
                }
            }
        }
    }
}