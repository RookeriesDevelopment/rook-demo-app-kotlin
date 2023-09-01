package com.rookmotion.rookconnectdemo.features.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentSelectorBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.setNavigateOnClick
import java.time.format.DateTimeFormatter

class SelectorFragment : Fragment() {

    private var _binding: FragmentSelectorBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel> { ViewModelFactory(serviceLocator) }
    private val userViewModel by viewModels<UserViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            authViewModel.authState.collect {
                when (it) {
                    AuthState.NotAuthorized -> {
                        // Ignored
                    }

                    AuthState.Loading -> {
                        binding.progress.root.isVisible = true
                        binding.auth.root.isVisible = false
                    }

                    is AuthState.Error -> {
                        binding.auth.authorizedUntil.text = it.message

                        binding.progress.root.isVisible = false
                        binding.auth.root.isVisible = true
                        binding.auth.retry.isVisible = true
                    }

                    is AuthState.Authorized -> {
                        val icon =
                            if (it.authorizationResult.authorization.isNotExpired) R.drawable.ic_verified
                            else R.drawable.ic_not_verified

                        val date =
                            DateTimeFormatter.ISO_ZONED_DATE_TIME.format(it.authorizationResult.authorization.authorizedUntil)

                        binding.auth.authorizedUntil.setCompoundDrawablesWithIntrinsicBounds(
                            icon, 0, 0, 0
                        )

                        binding.auth.authorizedUntil.text =
                            it.authorizationResult.origin.name.plus(" > ").plus(
                                getString(R.string.authorized_until_placeholder, date)
                            )

                        val features =
                            it.authorizationResult.authorization.features.flatMap { entry ->
                                listOf("${entry.key} ➞ ${entry.value}")
                            }

                        binding.auth.features.text = features.joinToString("\n")

                        binding.progress.root.isVisible = false
                        binding.auth.root.isVisible = true
                        binding.auth.retry.isVisible = false

                        if (userViewModel.userState.value !is UserState.Registered) {
                            userViewModel.registerUser(BuildConfig.USER_ID)
                        }
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
                        binding.progress.root.isVisible = true
                        binding.users.root.isVisible = false
                    }

                    is UserState.Error -> {
                        binding.users.hcUser.text = it.message

                        binding.progress.root.isVisible = false
                        binding.users.root.isVisible = true
                        binding.users.retry.isVisible = true
                    }

                    is UserState.Registered -> {
                        binding.users.hcUser.text = getString(
                            R.string.hc_placeholder_registered,
                            BuildConfig.USER_ID
                        )

                        binding.progress.root.isVisible = false
                        binding.users.root.isVisible = true
                        binding.users.retry.isVisible = false
                    }
                }
            }
        }

        binding.auth.retry.setOnClickListener {
            authViewModel.getAuthorization(BuildConfig.CLIENT_UUID)
        }

        binding.users.retry.setOnClickListener {
            userViewModel.registerUser(BuildConfig.USER_ID)
        }

        binding.healthConnectSdk.setNavigateOnClick(
            SelectorFragmentDirections.actionSelectorFragmentToHCAvailabilityFragment()
        )

        binding.connectionsPage.setNavigateOnClick(
            SelectorFragmentDirections.actionSelectorFragmentToConnectionsPageFragment()
        )
    }
}