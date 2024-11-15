package com.rookmotion.rookconnectdemo.features.sdkplayground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rookconnectdemo.common.openPlayStore
import com.rookmotion.rookconnectdemo.databinding.FragmentSdkPlaygroundBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

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

        RookPermissionsManager.registerPermissionsRequestLauncher(this) {
            Timber.d("Permissions request result: $it")
        }

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
            sdkPlaygroundViewModel.checkAvailability()
        }
        binding.downloadHealthConnect.setOnClickListener { openPlayStore(requireContext()) }

        repeatOnResume {
            sdkPlaygroundViewModel.permissions.collect { binding.checkPermissionsState.text = it }
        }

        binding.checkPermissions.setOnClickListener { sdkPlaygroundViewModel.checkPermissions() }
        binding.requestPermissions.setOnClickListener {
            lifecycleScope.launch { RookPermissionsManager.launchPermissionsRequest(requireContext()) }
        }
        binding.openHealthConnect.setOnClickListener { sdkPlaygroundViewModel.openHealthConnect() }

        repeatOnResume {
            sdkPlaygroundViewModel.syncHealthData.collect { binding.syncHealthDataState.text = it }
        }

        binding.syncHealthData.setOnClickListener {
            val localDate = LocalDate.parse(binding.date.text.toString())

            sdkPlaygroundViewModel.syncHealthData(localDate)
        }

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
        binding.date.setText(LocalDate.now().minusDays(1).toString())
    }

    override fun onDestroy() {
        RookPermissionsManager.unregisterPermissionsRequestLauncher()
        super.onDestroy()
    }
}