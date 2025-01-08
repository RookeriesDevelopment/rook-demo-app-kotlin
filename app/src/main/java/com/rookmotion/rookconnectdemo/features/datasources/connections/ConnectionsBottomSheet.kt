package com.rookmotion.rookconnectdemo.features.datasources.connections

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.BottomSheetConnectionsBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.toastLong

class ConnectionsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetConnectionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ConnectionAdapter

    private val viewModel by viewModels<ConnectionsViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetConnectionsBinding.inflate(inflater, container, false)
        adapter = ConnectionAdapter(
            onConnectClick = {
                val intent = CustomTabsIntent.Builder()
                    .setUrlBarHidingEnabled(true)
                    .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
//                    .setDownloadButtonEnabled(false)
//                    .setBookmarksButtonEnabled(false)
                    .build()

                intent.intent.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                }

                intent.launchUrl(requireContext(), Uri.parse(it))
            },
            onDisconnectClick = {
                viewModel.disconnectFromDataSource(it)
            }
        )

//        val windowMetrics = getWindowMetrics()
//        val peekHeightPx = (windowMetrics.bounds.height() * 0.25F).toInt()
//
        (dialog as BottomSheetDialog).behavior.apply {
//            peekHeight = peekHeightPx
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            viewModel.events.collect {
                when (it) {
                    ConnectionsEvents.DisconnectionNotSupported -> {
                        requireContext().toastLong(R.string.data_source_disconnection_not_supported)
                    }

                    is ConnectionsEvents.DisconnectionFailed -> {
                        requireContext().toastLong(R.string.data_source_disconnection_failed, it.dataSourceType.name)
                    }

                    is ConnectionsEvents.DisconnectionSuccess -> {
                        requireContext().toastLong(R.string.data_source_disconnection_success, it.dataSourceType.name)
                    }
                }
            }
        }

        repeatOnResume {
            viewModel.state.collect {
                when (it) {
                    ConnectionsState.Loading -> {
                        binding.progress.root.isVisible = true
                        binding.error.root.isVisible = false
                        binding.connections.isVisible = false
                    }

                    is ConnectionsState.Error -> {
                        binding.error.message.text = it.message

                        binding.progress.root.isVisible = false
                        binding.error.root.isVisible = true
                        binding.connections.isVisible = false
                    }

                    is ConnectionsState.Success -> {
                        binding.progress.root.isVisible = false
                        binding.error.root.isVisible = false
                        binding.connections.isVisible = true

                        adapter.submitList(it.dataSources)
                    }
                }
            }
        }

        binding.error.retry.setOnClickListener {
            viewModel.getAvailableDataSources()
        }
        binding.connections.setHasFixedSize(true)
        binding.connections.adapter = adapter
    }

    companion object {
        const val TAG = "ConnectionsBottomSheet"
    }
}
