package com.rookmotion.rookconnectdemo.features.permissions

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
import com.rookmotion.rookconnectdemo.common.openPlayStore
import com.rookmotion.rookconnectdemo.databinding.FragmentPermissionsBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.displayConsoleOutputUpdates
import com.rookmotion.rookconnectdemo.extension.openApplicationSettings
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class PermissionsFragment : Fragment() {

    private var _binding: FragmentPermissionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PermissionsViewModel> { ViewModelFactory(serviceLocator) }

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
            val dialogDisplayed = intent?.getBooleanExtra(
                /* name = */ RookPermissionsManager.EXTRA_ANDROID_PERMISSIONS_DIALOG_DISPLAYED,
                /* defaultValue = */ false
            ) ?: false

            val permissionsGranted = intent?.getBooleanExtra(
                /* name = */ RookPermissionsManager.EXTRA_ANDROID_PERMISSIONS_GRANTED,
                /* defaultValue = */ false
            ) ?: false

            viewModel.onAndroidPermissionsResult(dialogDisplayed, permissionsGranted)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionsBinding.inflate(inflater, container, false)

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
            consoleOutput = viewModel.hcAvailabilityOutput,
            receiver = binding.checkHealthConnectAvailabilityOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.checkHCPermissionsOutput,
            receiver = binding.checkHealthConnectPermissionsOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.checkHCPermissionsPartiallyOutput,
            receiver = binding.checkHealthConnectPermissionsPartiallyOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.requestHCPermissionsOutput,
            receiver = binding.requestHealthConnectPermissionsOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.revokeHCPermissionsOutput,
            receiver = binding.revokeHealthConnectPermissionsOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.openHealthConnectOutput,
            receiver = binding.openHealthConnectSettingsOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.checkAndroidPermissionsOutput,
            receiver = binding.checkAndroidPermissionsOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.requestAndroidPermissionsOutput,
            receiver = binding.requestAndroidPermissionsOutput,
        )

        binding.checkHealthConnectAvailability.setOnClickListener {
            viewModel.checkHealthConnectAvailability()
        }

        binding.downloadHealthConnect.setOnClickListener {
            openPlayStore(requireContext())
        }

        binding.checkHealthConnectPermissions.setOnClickListener {
            viewModel.checkHealthConnectPermissions()
        }

        binding.checkHealthConnectPermissionsPartially.setOnClickListener {
            viewModel.checkHealthConnectPermissionsPartially()
        }

        binding.requestHealthConnectPermissions.setOnClickListener {
            viewModel.requestHealthConnectPermissions()
        }

        binding.revokeHealthConnectPermissions.setOnClickListener {
            viewModel.revokeHealthConnectPermissions()
        }

        binding.openHealthConnectSettings.setOnClickListener {
            viewModel.openHealthConnectSettings()
        }

        binding.checkAndroidPermissions.setOnClickListener {
            viewModel.checkAndroidPermissions()
        }

        binding.requestAndroidPermissions.setOnClickListener {
            viewModel.requestAndroidPermissions(requireActivity())
        }

        binding.openAppSettings.setOnClickListener {
            requireContext().openApplicationSettings()
        }
    }
}
