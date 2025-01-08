package com.rookmotion.rookconnectdemo.features.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rookmotion.rookconnectdemo.databinding.FragmentConfigurationBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.displayConsoleOutputUpdates
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class ConfigurationFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ConfigurationViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            viewModel.enableNavigation.collect {
                binding.backgroundSteps.isEnabled = it
                binding.userManagement.isEnabled = it
                binding.dataSources.isEnabled = it
                binding.permissions.isEnabled = it
                binding.manuallySyncHealthData.isEnabled = it
                binding.continuousUpload.isEnabled = it
            }
        }

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.configurationOutput,
            receiver = binding.configurationOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.initializeOutput,
            receiver = binding.initializeOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.updateUserOutput,
            receiver = binding.updateUserOutput,
        )

        binding.setConfiguration.setOnClickListener { viewModel.setConfiguration() }
        binding.initialize.setOnClickListener { viewModel.initialize() }
        binding.updateUser.setOnClickListener {
            val userID = getUserID()

            if (userID != null) {
                viewModel.updateUserID(userID)
            }
        }

        binding.backgroundSteps.setOnClickListener {
            findNavController().navigate(
                ConfigurationFragmentDirections.actionConfigurationFragmentToBackgroundStepsFragment()
            )
        }
        binding.userManagement.setOnClickListener {
            findNavController().navigate(
                ConfigurationFragmentDirections.actionConfigurationFragmentToUserManagementFragment()
            )
        }
        binding.dataSources.setOnClickListener {
            findNavController().navigate(
                ConfigurationFragmentDirections.actionConfigurationFragmentToDataSourcesFragment()
            )
        }
        binding.permissions.setOnClickListener {
            findNavController().navigate(
                ConfigurationFragmentDirections.actionConfigurationFragmentToPermissionsFragment()
            )
        }
        binding.manuallySyncHealthData.setOnClickListener {
            findNavController().navigate(
                ConfigurationFragmentDirections.actionConfigurationFragmentToSyncFragment()
            )
        }
        binding.continuousUpload.setOnClickListener {
            findNavController().navigate(
                ConfigurationFragmentDirections.actionConfigurationFragmentToContinuousUploadFragment()
            )
        }
    }

    private fun getUserID(): String? {
        val text = binding.userId.text.toString().trim()

        return if (text.isEmpty()) {
            binding.userIdContainer.error = "Cannot be empty"
            null
        } else {
            binding.userIdContainer.error = null
            text
        }
    }
}
