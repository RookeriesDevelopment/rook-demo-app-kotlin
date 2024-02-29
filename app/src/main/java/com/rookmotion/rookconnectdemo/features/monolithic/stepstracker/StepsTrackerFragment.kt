package com.rookmotion.rookconnectdemo.features.monolithic.stepstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.sdk.RookStepsTracker
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.common.isDebug
import com.rookmotion.rookconnectdemo.databinding.FragmentStepsTrackerBinding
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

class StepsTrackerFragment : Fragment() {

    private var _binding: FragmentStepsTrackerBinding? = null
    private val binding get() = _binding!!

    private val stepsTrackerViewModel by viewModels<StepsTrackerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStepsTrackerBinding.inflate(inflater, container, false)

        if (isDebug) {
            Timber.plant(Timber.DebugTree())
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
            stepsTrackerViewModel.state.collectLatest {
                if (it.hasPermissions) {
                    binding.permissionsStatus.setText(R.string.permissions_are_granted)
                    binding.permissionsStatus.isChecked = true
                    binding.requestPermissions.isEnabled = false
                } else {
                    binding.permissionsStatus.setText(R.string.missing_permissions)
                    binding.permissionsStatus.isChecked = false
                    binding.requestPermissions.isEnabled = true
                    binding.requestPermissions.setOnClickListener {
                        stepsTrackerViewModel.requestStepsTrackerPermissions(requireContext())
                    }
                }

                if (it.isLoading) {
                    binding.serviceToggle.isEnabled = false
                } else {
                    binding.serviceToggle.isEnabled = it.isAvailable

                    if (it.isActive) {
                        binding.serviceStatus.isChecked = true
                        binding.serviceStatus.setText(R.string.tracker_is_running)
                        binding.serviceToggle.setText(R.string.stop_tracker)
                        binding.serviceToggle.setOnClickListener {
                            stepsTrackerViewModel.stopStepsTracker(requireContext())
                        }
                    } else {
                        binding.serviceStatus.isChecked = false
                        binding.serviceStatus.setText(R.string.tracker_is_stopped)
                        binding.serviceToggle.setText(R.string.start_tracker)
                        binding.serviceToggle.setOnClickListener {
                            stepsTrackerViewModel.startStepsTracker(requireContext())
                        }
                    }

                    binding.totalSteps.text = getString(R.string.total_steps_number, it.steps)
                }
            }
        }

        // This element (class, method or field) is not in stable state yet. It may be renamed, changed or even removed in a future version.
        repeatOnResume {
            RookStepsTracker.observeTodaySteps().collectLatest {
                Timber.i("observeTodaySteps: $it")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        stepsTrackerViewModel.checkStepsTrackerStatus(requireContext())
    }
}