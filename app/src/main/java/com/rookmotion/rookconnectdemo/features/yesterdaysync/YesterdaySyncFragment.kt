package com.rookmotion.rookconnectdemo.features.yesterdaysync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.RookYesterdaySyncPermissions
import com.rookmotion.rook.sdk.domain.enums.HealthConnectAvailability
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.common.openPlayStore
import com.rookmotion.rookconnectdemo.databinding.FragmentYesterdaySyncBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.openApplicationSettings
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.toastLong

class YesterdaySyncFragment : Fragment() {

    private var _binding: FragmentYesterdaySyncBinding? = null
    private val binding get() = _binding!!

    private val yesterdaySyncViewModel by viewModels<YesterdaySyncViewModel> {
        ViewModelFactory(serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentYesterdaySyncBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.downloadHealthConnect.setOnClickListener { openPlayStore(requireContext()) }

        repeatOnResume {
            if (RookYesterdaySyncPermissions.hasHealthConnectPermissions(requireContext())) {
                binding.healthConnectPermissions.isChecked = true
                binding.requestHealthConnectPermissions.isEnabled = false
            } else {
                binding.healthConnectPermissions.isChecked = false
                binding.requestHealthConnectPermissions.isEnabled = true
            }
        }

        binding.requestAndroidPermissions.setOnClickListener {
            val shouldRequest = RookYesterdaySyncPermissions.shouldRequestAndroidPermissions(requireActivity())

            if (shouldRequest) {
                RookYesterdaySyncPermissions.requestAndroidPermissions(requireContext())
            } else {
                requireContext().openApplicationSettings()
                requireContext().toastLong(R.string.give_permissions_manually)
            }
        }

        binding.requestHealthConnectPermissions.setOnClickListener {
            RookYesterdaySyncPermissions.requestHealthConnectPermissions(requireContext())
        }

        binding.openHealthConnect.setOnClickListener {
            yesterdaySyncViewModel.openHealthConnect()
        }
    }

    override fun onResume() {
        super.onResume()

        if (RookYesterdaySyncPermissions.hasAndroidPermissions(requireContext())) {
            binding.androidPermissions.isChecked = true
            binding.requestAndroidPermissions.isEnabled = false
        } else {
            binding.androidPermissions.isChecked = false
            binding.requestAndroidPermissions.isEnabled = true
        }

        val string = when (RookHealthPermissionsManager.checkAvailability(requireContext())) {
            HealthConnectAvailability.INSTALLED -> "Health Connect is installed!"
            HealthConnectAvailability.NOT_INSTALLED -> "Health Connect is not installed. Please download from the Play Store"
            else -> "This device is not compatible with health connect. Go back!"
        }

        binding.availabilityStatus.text = string

        checkYesterdaySyncAcceptation()
    }

    private fun checkYesterdaySyncAcceptation() {
        if (yesterdaySyncViewModel.userAcceptedYesterdaySync) {
            binding.yesterdaySyncStatus.isChecked = true
            binding.yesterdaySyncToggle.setText(R.string.disable_yesterday_sync)
            binding.yesterdaySyncToggle.setOnClickListener {
                yesterdaySyncViewModel.disableYesterdaySync()
                checkYesterdaySyncAcceptation()
            }
        } else {
            binding.yesterdaySyncStatus.isChecked = false
            binding.yesterdaySyncToggle.setText(R.string.enable_yesterday_sync)
            binding.yesterdaySyncToggle.setOnClickListener {
                yesterdaySyncViewModel.enableYesterdaySync()
                checkYesterdaySyncAcceptation()
            }
        }
    }
}