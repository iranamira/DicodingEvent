package com.example.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.datastore.DataStoreInstance
import com.example.dicodingevent.data.local.datastore.SettingsPreference
import com.example.dicodingevent.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val settingsPreference by lazy {
        SettingsPreference(DataStoreInstance.getInstance(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwitchButton()
        setupObservers()
    }

    private fun setupSwitchButton() {
        binding.swDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                settingsPreference.updateDarkMode(isChecked)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                settingsPreference.darkMode.collect { isEnabled ->
                    binding.swDarkMode.isChecked = isEnabled
                }
            }
            launch {
                settingsPreference.notification.collect { isEnabled ->
                    binding.swNotification.isChecked = isEnabled
                }
            }
        }
    }
}