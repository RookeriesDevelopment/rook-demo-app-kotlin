package com.rookmotion.rookconnectdemo.ui.health_connect.permissions

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
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.ui.common.DataState
import com.rookmotion.rookconnectdemo.utils.repeatOnResume
import com.rookmotion.rookconnectdemo.utils.serviceLocator
import com.rookmotion.rookconnectdemo.utils.snackLong

class HCPermissionsFragment : Fragment() {

    private var _binding: FragmentHcPermissionsBinding? = null
    private val binding get() = _binding!!

    private val hcPermissionsViewModel by viewModels<HCPermissionsViewModel> {
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
            hcPermissionsViewModel.hasPermissions.collect {
                when (it) {
                    DataState.None -> binding.action.isEnabled = false
                    DataState.Loading -> {
                        // Ignored
                    }
                    is DataState.Error -> binding.root.snackLong(
                        message = it.message,
                        action = getString(R.string.retry),
                        onClick = {
                            hcPermissionsViewModel.checkPermissions()
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
                                hcPermissionsViewModel.requestPermissions(requireActivity())
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

        binding.openHealthConnect.setOnClickListener { hcPermissionsViewModel.openHealthConnectSettings() }
    }

    override fun onResume() {
        super.onResume()
        hcPermissionsViewModel.checkPermissions()
    }

    private fun toPlayground() {
        findNavController().navigate(
            HCPermissionsFragmentDirections.actionHCPermissionsFragmentToHCPlaygroundFragment()
        )
    }
}