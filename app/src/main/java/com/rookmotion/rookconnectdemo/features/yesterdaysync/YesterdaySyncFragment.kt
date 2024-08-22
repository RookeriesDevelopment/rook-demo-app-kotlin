package com.rookmotion.rookconnectdemo.features.yesterdaysync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookPermissionsManager
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

    private val healthConnectBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val permissionsGranted = intent?.getBooleanExtra(
                /* name = */ RookPermissionsManager.EXTRA_HEALTH_CONNECT_PERMISSIONS_GRANTED,
                /* defaultValue = */ false
            ) ?: false

            yesterdaySyncViewModel.onHealthConnectPermissionsResult(permissionsGranted)
        }
    }

    private val androidBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val permissionsGranted = intent?.getBooleanExtra(
                /* name = */ RookPermissionsManager.EXTRA_ANDROID_PERMISSIONS_GRANTED,
                /* defaultValue = */ false
            ) ?: false

            yesterdaySyncViewModel.onAndroidPermissionsResult(permissionsGranted)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentYesterdaySyncBinding.inflate(inflater, container, false)

        ContextCompat.registerReceiver(
            requireContext(),
            healthConnectBroadcastReceiver,
            IntentFilter(RookPermissionsManager.ACTION_HEALTH_CONNECT_PERMISSIONS),
            ContextCompat.RECEIVER_EXPORTED,
        )

        ContextCompat.registerReceiver(
            requireContext(),
            androidBroadcastReceiver,
            IntentFilter(RookPermissionsManager.ACTION_ANDROID_PERMISSIONS),
            ContextCompat.RECEIVER_EXPORTED,
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireContext().unregisterReceiver(healthConnectBroadcastReceiver)
        requireContext().unregisterReceiver(androidBroadcastReceiver)

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            yesterdaySyncViewModel.hasHealthConnectPermissions.collect {
                if (it) {
                    binding.healthConnectPermissions.isChecked = true
                    binding.requestHealthConnectPermissions.isEnabled = false
                } else {
                    binding.healthConnectPermissions.isChecked = false
                    binding.requestHealthConnectPermissions.isEnabled = true
                }
            }
        }

        repeatOnResume {
            yesterdaySyncViewModel.hasAndroidPermissions.collect {
                if (it) {
                    binding.androidPermissions.isChecked = true
                    binding.requestAndroidPermissions.isEnabled = false
                } else {
                    binding.androidPermissions.isChecked = false
                    binding.requestAndroidPermissions.isEnabled = true
                }
            }
        }

        binding.downloadHealthConnect.setOnClickListener { openPlayStore(requireContext()) }

        binding.requestAndroidPermissions.setOnClickListener {
            val shouldRequest = RookPermissionsManager.shouldRequestAndroidPermissions(requireActivity())

            if (shouldRequest) {
                yesterdaySyncViewModel.requestAndroidPermissions()
            } else {
                requireContext().openApplicationSettings()
                requireContext().toastLong(R.string.give_permissions_manually)
            }
        }

        binding.requestHealthConnectPermissions.setOnClickListener {
            yesterdaySyncViewModel.requestHealthConnectPermissions()
        }

        binding.openHealthConnect.setOnClickListener {
            yesterdaySyncViewModel.openHealthConnect()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.availabilityStatus.text = when (yesterdaySyncViewModel.checkHealthConnectAvailability()) {
            HealthConnectAvailability.INSTALLED -> "Health Connect is installed!"
            HealthConnectAvailability.NOT_INSTALLED -> "Health Connect is not installed. Please download from the Play Store"
            else -> "This device is not compatible with health connect. Go back!"
        }

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