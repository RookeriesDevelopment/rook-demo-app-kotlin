package com.rookmotion.rookconnectdemo.features.continuousupload

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
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentContinuousUploadBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.displayConsoleOutputUpdates
import com.rookmotion.rookconnectdemo.extension.openApplicationSettings
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.toastLong

class ContinuousUploadFragment : Fragment() {

    private var _binding: FragmentContinuousUploadBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ContinuousUploadViewModel> { ViewModelFactory(serviceLocator) }

    private val healthConnectBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val permissionsGranted = intent?.getBooleanExtra(
                /* name = */ RookPermissionsManager.EXTRA_HEALTH_CONNECT_PERMISSIONS_GRANTED,
                /* defaultValue = */ false
            ) ?: false

            val permissionsPartiallyGranted = intent?.getBooleanExtra(
                /* name = */ RookPermissionsManager.EXTRA_HEALTH_CONNECT_PERMISSIONS_PARTIALLY_GRANTED,
                /* defaultValue = */ false
            ) ?: false

            viewModel.onHealthConnectPermissionsResult(permissionsGranted, permissionsPartiallyGranted)
        }
    }

    private val androidBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val permissionsGranted = intent?.getBooleanExtra(
                /* name = */ RookPermissionsManager.EXTRA_ANDROID_PERMISSIONS_GRANTED,
                /* defaultValue = */ false
            ) ?: false

            viewModel.onAndroidPermissionsResult(permissionsGranted)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContinuousUploadBinding.inflate(inflater, container, false)

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

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.continuousUploadOutput,
            receiver = binding.continuousUploadOutput,
        )

        repeatOnResume {
            viewModel.state.collect { state ->
                binding.androidPermissions.isChecked = state.hasAndroidPermissions
                binding.healthConnectPermissions.isChecked = state.hasHealthConnectPermissions
                binding.continuousUpload.isChecked = state.isContinuousUploadEnabled

                binding.enableContinuousUpload.isEnabled = !state.isContinuousUploadEnabled &&
                        state.hasAndroidPermissions &&
                        state.hasHealthConnectPermissions

                binding.disableContinuousUpload.isEnabled = state.isContinuousUploadEnabled
            }
        }

        binding.requestAndroidPermissions.setOnClickListener {
            val shouldRequestPermissions = RookPermissionsManager.shouldRequestAndroidPermissions(requireActivity())

            if (shouldRequestPermissions) {
                viewModel.requestAndroidPermissions()
            } else {
                requireContext().openApplicationSettings()
                requireContext().toastLong(R.string.give_permissions_manually)
            }
        }

        binding.requestHealthConnectPermissions.setOnClickListener {
            viewModel.requestHealthConnectPermissions()
        }

        binding.openHealthConnect.setOnClickListener {
            viewModel.openHealthConnectSettings()
        }

        binding.enableContinuousUpload.setOnClickListener {
            viewModel.enableContinuousUpload()
        }

        binding.disableContinuousUpload.setOnClickListener {
            viewModel.disableContinuousUpload()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
    }
}
