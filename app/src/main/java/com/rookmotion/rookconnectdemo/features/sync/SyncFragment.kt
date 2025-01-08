package com.rookmotion.rookconnectdemo.features.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rookmotion.rookconnectdemo.databinding.FragmentSyncBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.displayConsoleOutputUpdates
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import java.time.LocalDate

class SyncFragment : Fragment() {

    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SyncViewModel> { ViewModelFactory(serviceLocator) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.syncSummariesOutput,
            receiver = binding.syncSummariesOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.syncEventsOutput,
            receiver = binding.syncEventsOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.syncPendingSummariesOutput,
            receiver = binding.syncPendingSummariesOutput,
        )

        displayConsoleOutputUpdates(
            consoleOutput = viewModel.syncPendingEventsOutput,
            receiver = binding.syncPendingEventsOutput,
        )

        binding.summariesDate.setText(LocalDate.now().minusDays(1).toString())
        binding.eventsDate.setText(LocalDate.now().toString())

        binding.syncSummaries.setOnClickListener {
            val date = LocalDate.parse(binding.summariesDate.text.toString())

            viewModel.syncSummaries(date)
        }
        binding.syncEvents.setOnClickListener {
            val date = LocalDate.parse(binding.eventsDate.text.toString())

            viewModel.syncEvents(date)
        }
        binding.syncPendingSummaries.setOnClickListener {
            viewModel.syncPendingSummaries()
        }
        binding.syncPendingEvents.setOnClickListener {
            viewModel.syncPendingEvents()
        }
    }
}