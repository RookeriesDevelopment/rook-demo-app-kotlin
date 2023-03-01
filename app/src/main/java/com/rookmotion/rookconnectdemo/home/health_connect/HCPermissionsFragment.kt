package com.rookmotion.rookconnectdemo.home.health_connect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentHcPermissionsBinding
import com.rookmotion.rookconnectdemo.home.common.DataState
import com.rookmotion.rookconnectdemo.home.common.ViewModelFactory
import com.rookmotion.rookconnectdemo.utils.repeatOnResume
import com.rookmotion.rookconnectdemo.utils.serviceLocator
import com.rookmotion.rookconnectdemo.utils.snackLong

class HCPermissionsFragment : Fragment() {

    private var _binding: FragmentHcPermissionsBinding? = null
    private val binding get() = _binding!!

    private val healthConnectViewModel by viewModels<HealthConnectViewModel> {
        ViewModelFactory(requireActivity().serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHcPermissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            healthConnectViewModel.hasPermissions.collect {
                when (it) {
                    DataState.None -> binding.action.isEnabled = false
                    DataState.Loading -> {
                        // Ignored
                    }
                    is DataState.Error -> binding.root.snackLong(
                        message = it.message,
                        action = getString(R.string.retry),
                        onClick = {
                            healthConnectViewModel.checkPermissions()
                        },
                    )
                    is DataState.Success -> {
                        if (it.data) {
                            binding.action.text = getString(R.string.continue_text)
                            binding.action.setIconResource(R.drawable.ic_arrow_forward)
                            binding.action.setOnClickListener { toPlayground() }
                        } else {
                            binding.action.text = getString(R.string.request_permissions)
                            binding.action.icon = null
                            binding.action.setOnClickListener {
                                healthConnectViewModel.requestPermissions(requireActivity())
                            }
                        }

                        binding.message.text = getString(
                            R.string.permissions_granted_placeholder,
                            it.data.toString()
                        )
                        binding.action.isEnabled = true

                        binding.tip.isVisible = !it.data
                        binding.openHealthConnect.isVisible = !it.data
                    }
                }
            }
        }

        binding.openHealthConnect.setOnClickListener { healthConnectViewModel.openHealthConnectSettings() }
    }

    override fun onResume() {
        super.onResume()
        healthConnectViewModel.checkPermissions()
    }

    private fun toPlayground() {
        findNavController().navigate(
            HCPermissionsFragmentDirections.actionHCPermissionsFragmentToHCPlaygroundFragment()
        )
    }
}