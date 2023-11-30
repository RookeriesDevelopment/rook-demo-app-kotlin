package com.rookmotion.rookconnectdemo.features.modules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentModulesBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.clearCompoundDrawablesWithIntrinsicBounds
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.setNavigateOnClick
import com.rookmotion.rookconnectdemo.extension.setStartCompoundDrawableWithIntrinsicBounds

class ModulesFragment : Fragment() {

    private var _binding: FragmentModulesBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel> { ViewModelFactory(serviceLocator) }
    private val userViewModel by viewModels<UserViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentModulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            authViewModel.transmissionInitialization.collect {
                when (it) {
                    InitializationState.None -> {
                        binding.auth.rookTransmission.isVisible = false
                    }

                    InitializationState.Loading -> {
                        binding.auth.rookTransmission.setText(R.string.loading)
                        binding.auth.rookTransmission.clearCompoundDrawablesWithIntrinsicBounds()
                        binding.auth.rookTransmission.isVisible = true
                    }

                    is InitializationState.Success -> {
                        binding.auth.rookTransmission.text = "Transmission ➞ ${it.expirationDate}"
                        binding.auth.rookTransmission.setStartCompoundDrawableWithIntrinsicBounds(
                            R.drawable.ic_verified
                        )
                        binding.auth.rookTransmission.isVisible = true
                    }

                    is InitializationState.Error -> {
                        binding.auth.rookTransmission.text = it.message
                        binding.auth.rookTransmission.setStartCompoundDrawableWithIntrinsicBounds(
                            R.drawable.ic_not_verified
                        )
                        binding.auth.rookTransmission.isVisible = true
                    }
                }
            }
        }

        repeatOnResume {
            authViewModel.healthConnectInitialization.collect {
                when (it) {
                    InitializationState.None -> {
                        binding.auth.rookHealthConnect.isVisible = false
                    }

                    InitializationState.Loading -> {
                        binding.auth.rookHealthConnect.setText(R.string.loading)
                        binding.auth.rookHealthConnect.clearCompoundDrawablesWithIntrinsicBounds()
                        binding.auth.rookHealthConnect.isVisible = true
                    }

                    is InitializationState.Success -> {
                        binding.auth.rookHealthConnect.text =
                            "Health Connect ➞ ${it.expirationDate}"
                        binding.auth.rookHealthConnect.setStartCompoundDrawableWithIntrinsicBounds(
                            R.drawable.ic_verified
                        )
                        binding.auth.rookHealthConnect.isVisible = true
                    }

                    is InitializationState.Error -> {
                        binding.auth.rookHealthConnect.text = it.message
                        binding.auth.rookHealthConnect.setStartCompoundDrawableWithIntrinsicBounds(
                            R.drawable.ic_not_verified
                        )
                        binding.auth.rookHealthConnect.isVisible = true
                    }
                }
            }
        }

        repeatOnResume {
            authViewModel.usersInitialization.collect {
                when (it) {
                    InitializationState.None -> {
                        binding.auth.rookUsers.isVisible = false
                    }

                    InitializationState.Loading -> {
                        binding.auth.rookUsers.setText(R.string.loading)
                        binding.auth.rookUsers.clearCompoundDrawablesWithIntrinsicBounds()
                        binding.auth.rookUsers.isVisible = true
                    }

                    is InitializationState.Success -> {
                        binding.auth.rookUsers.text = "Users ➞ ${it.expirationDate}"
                        binding.auth.rookUsers.setStartCompoundDrawableWithIntrinsicBounds(
                            R.drawable.ic_verified
                        )
                        binding.auth.rookUsers.isVisible = true

                        userViewModel.registerUser(BuildConfig.USER_ID)
                    }

                    is InitializationState.Error -> {
                        binding.auth.rookUsers.text = it.message
                        binding.auth.rookUsers.setStartCompoundDrawableWithIntrinsicBounds(
                            R.drawable.ic_not_verified
                        )
                        binding.auth.rookUsers.isVisible = true
                    }
                }
            }
        }

        repeatOnResume {
            userViewModel.userState.collect {
                when (it) {
                    UserState.None -> {
                        // Ignored
                    }

                    UserState.Loading -> {
                        binding.users.progress.isVisible = true
                    }

                    is UserState.Error -> {
                        binding.users.hcUser.text = it.message

                        binding.users.progress.isVisible = false
                        binding.users.deleteUser.isVisible = false
                        binding.users.retry.isVisible = true
                    }

                    is UserState.Registered -> {
                        binding.users.hcUser.text = getString(
                            R.string.hc_placeholder_registered,
                            BuildConfig.USER_ID
                        )

                        binding.users.progress.isVisible = false
                        binding.users.deleteUser.isVisible = true
                        binding.users.retry.isVisible = false
                    }
                }
            }
        }

        binding.auth.retry.setOnClickListener {
            authViewModel.initializeTransmission(requireContext(), BuildConfig.CLIENT_UUID)
            authViewModel.initializeHealthConnect(
                requireContext(),
                BuildConfig.USER_ID,
                BuildConfig.CLIENT_UUID,
                BuildConfig.SECRET_KEY,
            )
            authViewModel.initializeUsers(requireContext(), BuildConfig.CLIENT_UUID)
        }

        binding.users.deleteUser.setOnClickListener {
            userViewModel.deleteUser(BuildConfig.USER_ID)
        }

        binding.users.retry.setOnClickListener {
            userViewModel.registerUser(BuildConfig.USER_ID)
        }

        binding.healthConnectSdk.setNavigateOnClick(
            ModulesFragmentDirections.actionModulesFragmentToHCAvailabilityFragment()
        )

        authViewModel.initializeTransmission(requireContext(), BuildConfig.CLIENT_UUID)
        authViewModel.initializeHealthConnect(
            requireContext(),
            BuildConfig.USER_ID,
            BuildConfig.CLIENT_UUID,
            BuildConfig.SECRET_KEY,
        )
        authViewModel.initializeUsers(requireContext(), BuildConfig.CLIENT_UUID)
    }
}