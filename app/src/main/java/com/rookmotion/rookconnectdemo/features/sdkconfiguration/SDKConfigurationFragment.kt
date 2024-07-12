package com.rookmotion.rookconnectdemo.features.sdkconfiguration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rookmotion.rookconnectdemo.databinding.FragmentSdkConfigurationBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class SDKConfigurationFragment : Fragment() {

    private var _binding: FragmentSdkConfigurationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SDKConfigurationViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSdkConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            viewModel.configuration.collect { binding.setConfigurationState.text = it }
        }

        binding.setConfiguration.setOnClickListener { viewModel.setConfiguration() }

        repeatOnResume {
            viewModel.initialize.collect { binding.initializeState.text = it }
        }

        binding.initialize.setOnClickListener { viewModel.initialize() }

        repeatOnResume {
            viewModel.user.collect { binding.updateUserState.text = it }
        }

        binding.updateUser.setOnClickListener {
            val userID = getUserID()

            if (userID != null) {
                viewModel.updateUserID(userID)
            }
        }

        repeatOnResume {
            viewModel.enableNavigation.collect {
                binding.healthConnect.isEnabled = it
                binding.stepsService.isEnabled = it
                binding.yesterdaySyncPermissions.isEnabled = it
                binding.connectionsPage.isEnabled = it
                binding.connectionsPageBuild.isEnabled = it
            }
        }

        binding.healthConnect.setOnClickListener {
            findNavController().navigate(
                SDKConfigurationFragmentDirections.actionSDKConfigurationFragmentToSdkPlaygroundFragment()
            )
        }

        binding.stepsService.setOnClickListener {
            findNavController().navigate(
                SDKConfigurationFragmentDirections.actionSDKConfigurationFragmentToBackgroundStepsFragment()
            )
        }

        binding.yesterdaySyncPermissions.setOnClickListener {
            findNavController().navigate(
                SDKConfigurationFragmentDirections.actionSDKConfigurationFragmentToYesterdaySyncFragment()
            )
        }

        binding.connectionsPage.setOnClickListener {
            findNavController().navigate(
                SDKConfigurationFragmentDirections.actionSDKConfigurationFragmentToConnectionsPageFragment()
            )
        }

        binding.connectionsPageBuild.setOnClickListener {
            serviceLocator.rookDataSources.presentDataSourceView("https://tryrook.io")
        }
    }

    private fun getUserID(): String? {
        val text = binding.userId.text.toString().trim()

        return if (text.isEmpty()) {
            binding.userIdContainer.error = "Cannot be empty"
            binding.userIdContainer.isErrorEnabled = true

            null
        } else {
            binding.userIdContainer.error = null
            binding.userIdContainer.isErrorEnabled = false

            text
        }
    }
}
