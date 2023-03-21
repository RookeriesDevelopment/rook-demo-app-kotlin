package com.rookmotion.rookconnectdemo.home.selector

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
import com.rookmotion.rookconnectdemo.home.common.BasicState
import com.rookmotion.rookconnectdemo.home.common.DataState
import com.rookmotion.rookconnectdemo.home.common.ViewModelFactory
import com.rookmotion.rookconnectdemo.home.selector.SelectorFragmentDirections.Companion.actionSelectorFragmentToHCAvailabilityFragment
import com.rookmotion.rookconnectdemo.utils.repeatOnResume
import com.rookmotion.rookconnectdemo.utils.serviceLocator
import com.rookmotion.rookconnectdemo.utils.setNavigateOnClick
import java.time.format.DateTimeFormatter

class SelectorFragment : Fragment() {

    private var _binding: FragmentSelectorBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel> {
        ViewModelFactory(requireActivity().serviceLocator)
    }

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
            authViewModel.authorization.collect {
                when (it) {
                    DataState.None -> {
                        // Ignored
                    }
                    DataState.Loading -> {
                        binding.progress.root.isVisible = true
                        binding.auth.root.isVisible = false
                    }
                    is DataState.Error -> {
                        binding.auth.authorizedUntil.text = it.message

                        binding.progress.root.isVisible = false
                        binding.auth.root.isVisible = true
                        binding.auth.retry.isVisible = true
                    }
                    is DataState.Success -> {
                        val icon = if (it.data.authorization.isNotExpired) R.drawable.ic_verified
                        else R.drawable.ic_not_verified

                        val date = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(it.data.authorization.authorizedUntil)

                        binding.auth.authorizedUntil.setCompoundDrawablesWithIntrinsicBounds(
                            icon, 0, 0, 0
                        )

                        binding.auth.authorizedUntil.text = it.data.origin.name.plus(" > ").plus(
                            getString(R.string.authorized_until_placeholder, date)
                        )

                        val features = it.data.authorization.features.flatMap { entry ->
                            listOf("${entry.key} ➞ ${entry.value}")
                        }

                        binding.auth.features.text = features.joinToString("\n")

                        binding.progress.root.isVisible = false
                        binding.auth.root.isVisible = true
                        binding.auth.retry.isVisible = false

                        if (authViewModel.user.value !is BasicState.Success) {
                            authViewModel.registerUser(BuildConfig.USER_ID)
                        }
                    }
                }
            }
        }

        repeatOnResume {
            authViewModel.user.collect {
                when (it) {
                    BasicState.None -> {
                        // Ignored
                    }
                    BasicState.Loading -> {
                        binding.progress.root.isVisible = true
                        binding.users.root.isVisible = false
                    }
                    is BasicState.Error -> {
                        binding.users.hcUser.text = it.message

                        binding.progress.root.isVisible = false
                        binding.users.root.isVisible = true
                        binding.users.retry.isVisible = true
                    }
                    is BasicState.Success -> {
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
            authViewModel.registerUser(BuildConfig.USER_ID)
        }

        binding.healthConnectSdk.setNavigateOnClick(actionSelectorFragmentToHCAvailabilityFragment())
    }
}