package com.rookmotion.rookconnectdemo.ui.health_connect.availability

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.rookmotion.rook.health_connect.domain.enums.HCAvailabilityStatus
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentHcAvailabilityBinding
import com.rookmotion.rookconnectdemo.ui.common.DataState
import com.rookmotion.rookconnectdemo.utils.repeatOnResume
import com.rookmotion.rookconnectdemo.utils.snackLong

class HCAvailabilityFragment : Fragment() {

    private var _binding: FragmentHcAvailabilityBinding? = null
    private val binding get() = _binding!!

    private val hcAvailabilityViewModel by viewModels<HCAvailabilityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHcAvailabilityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            hcAvailabilityViewModel.isAvailable.collect {
                when (it) {
                    DataState.None -> binding.action.isEnabled = false
                    DataState.Loading -> {
                        // Ignored
                    }
                    is DataState.Error -> binding.root.snackLong(
                        message = it.message,
                        action = getString(R.string.retry),
                        onClick = {
                            hcAvailabilityViewModel.checkAvailability(requireContext())
                        },
                    )
                    is DataState.Success -> {
                        when (it.data) {
                            HCAvailabilityStatus.NOT_SUPPORTED -> {
                                binding.action.text = getString(R.string.go_back)
                                binding.action.setIconResource(R.drawable.ic_arrow_back)
                                binding.action.iconGravity = MaterialButton.ICON_GRAVITY_START
                                binding.action.setOnClickListener { findNavController().navigateUp() }
                            }
                            HCAvailabilityStatus.NOT_INSTALLED -> {
                                binding.action.text = getString(R.string.install_now)
                                binding.action.setIconResource(R.drawable.ic_download)
                                binding.action.setOnClickListener { openPlayStore() }
                            }
                            HCAvailabilityStatus.INSTALLED -> {
                                binding.action.text = getString(R.string.continue_text)
                                binding.action.setIconResource(R.drawable.ic_arrow_forward)
                                binding.action.setOnClickListener { toPermissions() }
                            }
                        }

                        binding.message.text = getString(
                            R.string.health_connect_placeholder,
                            it.data.name
                        )
                        binding.action.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hcAvailabilityViewModel.checkAvailability(requireContext())
    }

    private fun openPlayStore() {
        requireContext().startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")
            )
        )
    }

    private fun toPermissions() {
        findNavController().navigate(
            HCAvailabilityFragmentDirections.actionHCAvailabilityFragmentToHCPermissionsFragment()
        )
    }
}