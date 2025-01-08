package com.rookmotion.rookconnectdemo.features.backgroundsteps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentBackgroundStepsBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.openApplicationSettings
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.toastLong
import kotlinx.coroutines.flow.collectLatest

class BackgroundStepsFragment : Fragment() {

    private var _binding: FragmentBackgroundStepsBinding? = null
    private val binding get() = _binding!!

    private val backgroundStepsViewModel by viewModels<BackgroundStepsViewModel> {
        ViewModelFactory(serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBackgroundStepsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            backgroundStepsViewModel.state.collectLatest {
                if (it.hasAndroidPermissions) {
                    binding.permissionsStatus.setText(R.string.permissions_are_granted)
                    binding.permissionsStatus.isChecked = true
                    binding.requestPermissions.isEnabled = false
                }

                if (!it.hasAndroidPermissions) {
                    binding.permissionsStatus.setText(R.string.missing_permissions)
                    binding.permissionsStatus.isChecked = false
                    binding.requestPermissions.isEnabled = true
                    binding.requestPermissions.setOnClickListener {
                        val shouldRequest = RookPermissionsManager.shouldRequestAndroidPermissions(requireActivity())

                        if (shouldRequest) {
                            backgroundStepsViewModel.requestAndroidPermissions()
                        } else {
                            requireContext().openApplicationSettings()
                            requireContext().toastLong(R.string.give_permissions_manually)
                        }
                    }
                }

                binding.serviceToggle.isEnabled = it.isStepsServiceAvailable &&
                        it.hasAndroidPermissions &&
                        !it.isLoading

                binding.syncTodaySteps.isEnabled = it.isTrackingSteps && !it.isLoading

                binding.currentDaySteps.text = getString(R.string.current_day_steps, it.steps)

                if (it.isTrackingSteps) {
                    binding.serviceStatus.isChecked = true
                    binding.serviceStatus.setText(R.string.background_steps_is_running)
                    binding.serviceToggle.setText(R.string.stop_background_steps)
                    binding.serviceToggle.setOnClickListener {
                        backgroundStepsViewModel.disableBackgroundSteps()
                    }

                    binding.syncTodaySteps.setOnClickListener {
                        backgroundStepsViewModel.syncTodaySteps()
                    }
                } else {
                    binding.serviceStatus.isChecked = false
                    binding.serviceStatus.setText(R.string.background_steps_is_stopped)
                    binding.serviceToggle.setText(R.string.start_background_steps)
                    binding.serviceToggle.setOnClickListener {
                        backgroundStepsViewModel.enableBackgroundSteps()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        backgroundStepsViewModel.checkStepsServiceStatus()
    }
}