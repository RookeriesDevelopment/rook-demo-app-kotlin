package com.rookmotion.rookconnectdemo.features.healthconnect.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rook.health_connect.framework.health.permissions.HCPermission
import com.rookmotion.rook.health_connect.framework.health.permissions.HCPermissionsRequestLauncher
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentHcPermissionsBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.setNavigateOnClick

class HCPermissionsFragment : Fragment() {

    private var _binding: FragmentHcPermissionsBinding? = null
    private val binding get() = _binding!!

    private val hcPermissionsViewModel by viewModels<HCPermissionsViewModel> {
        ViewModelFactory(serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHcPermissionsBinding.inflate(inflater, container, false)

        HCPermissionsRequestLauncher.register(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            hcPermissionsViewModel.permissionsState.collect {
                when (it) {
                    PermissionsState.None -> binding.action.isEnabled = false
                    PermissionsState.Loading -> {
                        // Ignored
                    }

                    is PermissionsState.Success -> {
                        if (it.hasPermissions) {
                            binding.action.text = getString(R.string.continue_text)
                            binding.action.setIconResource(R.drawable.ic_arrow_forward)
                            binding.action.setNavigateOnClick(
                                HCPermissionsFragmentDirections.actionHCPermissionsFragmentToHCPlaygroundFragment()
                            )
                        } else {
                            binding.action.text = getString(R.string.request_permissions)
                            binding.action.icon = null
                            binding.action.setOnClickListener {
                                HCPermissionsRequestLauncher.launch(HCPermission.ALL)
                            }
                        }

                        binding.message.text = getString(
                            R.string.permissions_granted_placeholder,
                            it.hasPermissions.toString()
                        )
                        binding.action.isEnabled = true

                        binding.tip.isVisible = !it.hasPermissions
                        binding.openHealthConnect.isVisible = !it.hasPermissions
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

    override fun onDestroy() {
        HCPermissionsRequestLauncher.unregister()
        super.onDestroy()
    }
}