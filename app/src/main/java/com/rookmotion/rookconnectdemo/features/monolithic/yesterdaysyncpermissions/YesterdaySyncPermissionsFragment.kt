package com.rookmotion.rookconnectdemo.features.monolithic.yesterdaysyncpermissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookYesterdaySyncPermissions
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

        if (RookYesterdaySyncPermissions.hasAndroidPermissions(requireContext())) {
            binding.androidPermissions.isChecked = true
            binding.requestAndroidPermissions.isEnabled = false
        } else {
            binding.androidPermissions.isChecked = false
            binding.requestAndroidPermissions.isEnabled = true
        }

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
}