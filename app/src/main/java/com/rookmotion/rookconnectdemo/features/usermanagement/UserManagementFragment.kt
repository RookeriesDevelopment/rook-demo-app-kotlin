package com.rookmotion.rookconnectdemo.features.usermanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentUserManagementBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.displayConsoleOutputUpdates
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator

class UserManagementFragment : Fragment() {

    private var _binding: FragmentUserManagementBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UserManagementViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            viewModel.userID.collect {
                binding.userId.text = getString(R.string.user_id_placeholder, it)
            }
        }

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.syncUserTimeZoneOutput,
            receiver = binding.syncUserTimezoneOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.clearUserOutput,
            receiver = binding.clearUserIdOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.deleteUserOutput,
            receiver = binding.deleteUserFromRookOutput,
        )

        binding.syncUserTimezone.setOnClickListener {
            viewModel.syncUserTimeZone()
        }
        binding.clearUserId.setOnClickListener {
            viewModel.clearUserID()
        }
        binding.deleteUserFromRook.setOnClickListener {
            viewModel.deleteUserFromRook()
        }
    }
}
