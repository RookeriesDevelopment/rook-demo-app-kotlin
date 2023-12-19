package com.rookmotion.rookconnectdemo.features.monolithic.sdkplayground

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rookconnectdemo.databinding.FragmentSdkPlaygroundBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class SDKPlaygroundFragment : Fragment() {

    private var _binding: FragmentSdkPlaygroundBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SDKPlaygroundViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSdkPlaygroundBinding.inflate(inflater, container, false)

        viewModel.registerPermissionsRequestLauncher(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            viewModel.availability.collect { binding.checkAvailabilityState.text = it }
        }

        binding.checkAvailability.setOnClickListener { viewModel.checkAvailability(requireContext()) }
        binding.downloadHealthConnect.setOnClickListener { openPlayStore() }

        repeatOnResume {
            viewModel.permissions.collect { binding.checkPermissionsState.text = it }
        }

        binding.checkPermissions.setOnClickListener { viewModel.checkPermissions() }
        binding.requestPermissions.setOnClickListener { viewModel.launchPermissionsRequest() }
        binding.openHealthConnect.setOnClickListener { viewModel.openHealthConnect() }

        repeatOnResume {
            viewModel.syncHealthData.collect { binding.syncHealthDataState.text = it }
        }

        binding.syncHealthData.setOnClickListener { viewModel.syncHealthData() }

        repeatOnResume {
            viewModel.pendingSummaries.collect { binding.syncPendingSummariesState.text = it }
        }

        binding.syncPendingSummaries.setOnClickListener { viewModel.syncPendingSummaries() }

        repeatOnResume {
            viewModel.pendingEvents.collect { binding.syncPendingEventsState.text = it }
        }

        binding.syncPendingEvents.setOnClickListener { viewModel.syncPendingEvents() }
    }

    override fun onDestroy() {
        viewModel.unregisterPermissionsRequestLauncher()
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