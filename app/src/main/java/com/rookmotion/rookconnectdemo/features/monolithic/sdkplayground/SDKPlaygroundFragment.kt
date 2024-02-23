package com.rookmotion.rookconnectdemo.features.monolithic.sdkplayground

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.domain.enums.HealthPermission
import com.rookmotion.rookconnectdemo.databinding.FragmentSdkPlaygroundBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class SDKPlaygroundFragment : Fragment() {

    private var _binding: FragmentSdkPlaygroundBinding? = null
    private val binding get() = _binding!!

    private val sdkPlaygroundViewModel by viewModels<SDKPlaygroundViewModel> {
        ViewModelFactory(serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSdkPlaygroundBinding.inflate(inflater, container, false)

        RookHealthPermissionsManager.registerPermissionsRequestLauncher(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            sdkPlaygroundViewModel.availability.collect { binding.checkAvailabilityState.text = it }
        }

        binding.checkAvailability.setOnClickListener {
            sdkPlaygroundViewModel.checkAvailability(requireContext())
        }
        binding.downloadHealthConnect.setOnClickListener { openPlayStore() }

        repeatOnResume {
            sdkPlaygroundViewModel.permissions.collect { binding.checkPermissionsState.text = it }
        }

        binding.checkPermissions.setOnClickListener { sdkPlaygroundViewModel.checkPermissions() }
        binding.requestPermissions.setOnClickListener {
            RookHealthPermissionsManager.launchPermissionsRequest(HealthPermission.ALL)
        }
        binding.openHealthConnect.setOnClickListener { sdkPlaygroundViewModel.openHealthConnect() }

        repeatOnResume {
            sdkPlaygroundViewModel.syncHealthData.collect { binding.syncHealthDataState.text = it }
        }

        binding.syncHealthData.setOnClickListener { sdkPlaygroundViewModel.syncHealthData() }

        repeatOnResume {
            sdkPlaygroundViewModel.pendingSummaries.collect {
                binding.syncPendingSummariesState.text = it
            }
        }

        binding.syncPendingSummaries.setOnClickListener { sdkPlaygroundViewModel.syncPendingSummaries() }

        repeatOnResume {
            sdkPlaygroundViewModel.pendingEvents.collect {
                binding.syncPendingEventsState.text = it
            }
        }

        binding.syncPendingEvents.setOnClickListener { sdkPlaygroundViewModel.syncPendingEvents() }
    }

    override fun onDestroy() {
        RookHealthPermissionsManager.unregisterPermissionsRequestLauncher()
        super.onDestroy()
    }

    private fun openPlayStore() {
        requireContext().startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")
            )
        )
    }
}