package com.rookmotion.rookconnectdemo.features.backgroundsteps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookStepsPermissions
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
                if (it.hasPermissions) {
                    binding.permissionsStatus.setText(R.string.permissions_are_granted)
                    binding.permissionsStatus.isChecked = true
                    binding.requestPermissions.isEnabled = false
                } else {
                    binding.permissionsStatus.setText(R.string.missing_permissions)
                    binding.permissionsStatus.isChecked = false
                    binding.requestPermissions.isEnabled = true
                    binding.requestPermissions.setOnClickListener {
                        val shouldRequest = RookStepsPermissions.shouldRequestPermissions(requireActivity())

                        if (shouldRequest) {
                            backgroundStepsViewModel.requestStepsPermissions()
                        } else {
                            requireContext().openApplicationSettings()
                            requireContext().toastLong(R.string.give_permissions_manually)
                        }
                    }
                }

                if (it.isLoading) {
                    binding.serviceToggle.isEnabled = false
                } else {
                    binding.serviceToggle.isEnabled = it.isAvailable

                    if (it.isActive) {
                        binding.serviceStatus.isChecked = true
                        binding.serviceStatus.setText(R.string.background_steps_is_running)
                        binding.serviceToggle.setText(R.string.stop_background_steps)
                        binding.serviceToggle.setOnClickListener {
                            backgroundStepsViewModel.stopStepsService()
                        }
                    } else {
                        binding.serviceStatus.isChecked = false
                        binding.serviceStatus.setText(R.string.background_steps_is_stopped)
                        binding.serviceToggle.setText(R.string.start_background_steps)
                        binding.serviceToggle.setOnClickListener {
                            backgroundStepsViewModel.startStepsService()
                        }
                    }

                    binding.currentDaySteps.text = getString(R.string.current_day_steps, it.steps)
                }
            }
        }

        backgroundStepsViewModel.syncTodaySteps()
    }

    override fun onResume() {
        super.onResume()
        backgroundStepsViewModel.checkStepsServiceStatus()
    }
}