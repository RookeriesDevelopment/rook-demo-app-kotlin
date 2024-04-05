package com.rookmotion.rookconnectdemo.features.yesterdaysyncpermissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.RookYesterdaySyncPermissions
import com.rookmotion.rook.sdk.domain.enums.AvailabilityStatus
import com.rookmotion.rookconnectdemo.common.openPlayStore
import com.rookmotion.rookconnectdemo.databinding.FragmentYesterdaySyncPermissionsBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class YesterdaySyncPermissionsFragment : Fragment() {

    private var _binding: FragmentYesterdaySyncPermissionsBinding? = null
    private val binding get() = _binding!!

    private val yesterdaySyncPermissionsViewModel by viewModels<YesterdaySyncPermissionsViewModel> {
        ViewModelFactory(serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentYesterdaySyncPermissionsBinding.inflate(inflater, container, false)

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
            RookYesterdaySyncPermissions.requestAndroidPermissions(requireContext())
        }

        binding.requestHealthConnectPermissions.setOnClickListener {
            RookYesterdaySyncPermissions.requestHealthConnectPermissions(requireContext())
        }

        binding.openHealthConnect.setOnClickListener {
            yesterdaySyncPermissionsViewModel.openHealthConnect()
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
            AvailabilityStatus.INSTALLED -> "Health Connect is installed!"
            AvailabilityStatus.NOT_INSTALLED -> "Health Connect is not installed. Please download from the Play Store"
            else -> "This device is not compatible with health connect. Go back!"
        }

        binding.availabilityStatus.text = string
    }
}