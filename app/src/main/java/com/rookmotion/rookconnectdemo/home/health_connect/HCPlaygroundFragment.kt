package com.rookmotion.rookconnectdemo.home.health_connect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentHcPlaygroundBinding
import com.rookmotion.rookconnectdemo.home.common.BasicState
import com.rookmotion.rookconnectdemo.home.common.DataState
import com.rookmotion.rookconnectdemo.home.common.ViewModelFactory
import com.rookmotion.rookconnectdemo.utils.repeatOnResume
import com.rookmotion.rookconnectdemo.utils.serviceLocator
import com.rookmotion.rookconnectdemo.utils.snackLong
import com.rookmotion.rookconnectdemo.utils.snackShort
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class HCPlaygroundFragment : Fragment() {

    private var _binding: FragmentHcPlaygroundBinding? = null
    private val binding get() = _binding!!

    private val healthConnectViewModel by viewModels<HealthConnectViewModel> {
        ViewModelFactory(requireActivity().serviceLocator)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHcPlaygroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initWidgets()
    }

    override fun onResume() {
        super.onResume()
        healthConnectViewModel.getDataLastDate()
    }

    private fun initListeners() {
        repeatOnResume {
            healthConnectViewModel.dataLastDate.collect {
                when (it) {
                    DataState.None -> {
                        binding.sleepSummaryDate.isVisible = false
                        binding.physicalSummaryDate.isVisible = false
                        binding.physicalEventsDate.isVisible = false
                        binding.bodySummaryDate.isVisible = false
                    }
                    DataState.Loading -> {
                        // Ignored
                    }
                    is DataState.Error -> binding.root.snackLong(
                        message = it.message,
                        action = getString(R.string.retry),
                        onClick = {
                            healthConnectViewModel.getDataLastDate()
                        },
                    )
                    is DataState.Success -> {
                        binding.sleepSummaryDate.setOnClickListener { _ ->
                            showCalendar(it.data.sleepSummaryDate) { date ->
                                healthConnectViewModel.getSleep(date)
                            }
                        }

                        binding.physicalSummaryDate.setOnClickListener { _ ->
                            showCalendar(it.data.physicalSummaryDate) { date ->
                                healthConnectViewModel.getPhysical(date)
                            }
                        }

                        binding.physicalEventsDate.setOnClickListener { _ ->
                            showCalendar(it.data.physicalEventsDate) { date ->
                                healthConnectViewModel.getPhysicalEvents(date)
                            }
                        }

                        binding.bodySummaryDate.setOnClickListener { _ ->
                            showCalendar(it.data.bodySummaryDate) { date ->
                                healthConnectViewModel.getBody(date)
                            }
                        }
                        binding.sleepSummaryDate.isVisible = true
                        binding.physicalSummaryDate.isVisible = true
                        binding.physicalEventsDate.isVisible = true
                        binding.bodySummaryDate.isVisible = true
                    }
                }
            }
        }

        repeatOnResume {
            healthConnectViewModel.sleepState.collect {
                binding.sleepSummaryDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.sleepSummary.text = it.extracted.toString()

                    binding.enqueueSleep.setOnClickListener { _ ->
                        healthConnectViewModel.enqueueSleep(it.extracted)
                    }
                } else {
                    binding.sleepSummary.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueueSleep.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued != null) {
                    binding.root.snackShort(
                        getString(
                            R.string.placeholder_enqueued_placeholder,
                            "sleep",
                            "${it.enqueued}"
                        )
                    )
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            healthConnectViewModel.physicalState.collect {
                binding.physicalSummaryDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.physicalSummary.text = it.extracted.toString()

                    binding.enqueuePhysical.setOnClickListener { _ ->
                        healthConnectViewModel.enqueuePhysical(it.extracted)
                    }
                } else {
                    binding.physicalSummary.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueuePhysical.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued != null) {
                    binding.root.snackShort(
                        getString(
                            R.string.placeholder_enqueued_placeholder,
                            "physical",
                            "${it.enqueued}"
                        )
                    )
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            healthConnectViewModel.physicalEventsState.collect {
                binding.physicalEventsDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.physicalEvents.text = it.extracted.toString()

                    binding.enqueuePhysicalEvents.setOnClickListener { _ ->
                        healthConnectViewModel.enqueuePhysicalEvents(it.extracted)
                    }
                } else {
                    binding.physicalEvents.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueuePhysicalEvents.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued != null) {
                    binding.root.snackShort(
                        getString(
                            R.string.placeholder_enqueued_placeholder,
                            "physical events",
                            "${it.enqueued}"
                        )
                    )
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            healthConnectViewModel.bodyState.collect {
                binding.bodySummaryDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.bodySummary.text = it.extracted.toString()

                    binding.enqueueBody.setOnClickListener { _ ->
                        healthConnectViewModel.enqueueBody(it.extracted)
                    }
                } else {
                    binding.bodySummary.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueueBody.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued != null) {
                    binding.root.snackShort(
                        getString(
                            R.string.placeholder_enqueued_placeholder,
                            "body",
                            "${it.enqueued}"
                        )
                    )
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            healthConnectViewModel.uploadState.collect {
                when (it) {
                    is BasicState.None -> {
                        // Ignored
                    }
                    BasicState.Loading -> binding.upload.isEnabled = false
                    is BasicState.Error -> {
                        binding.root.snackLong(it.message, getString(R.string.retry)) {
                            healthConnectViewModel.uploadData()
                        }
                        binding.upload.isEnabled = true
                    }
                    BasicState.Success -> {
                        binding.root.snackShort("uploaded")
                        binding.upload.isEnabled = true
                    }
                }
            }
        }

        repeatOnResume {
            healthConnectViewModel.clearQueueState.collect {
                when (it) {
                    BasicState.None -> {
                        // Ignored
                    }
                    BasicState.Loading -> binding.clear.isEnabled = false
                    is BasicState.Error -> {
                        binding.root.snackLong(it.message, getString(R.string.retry)) {
                            healthConnectViewModel.clearData()
                        }
                        binding.clear.isEnabled = true
                    }
                    BasicState.Success -> {
                        binding.root.snackShort("Cleared")
                        binding.clear.isEnabled = true
                    }
                }
            }
        }
    }

    private fun initWidgets() {
        binding.upload.setOnClickListener { healthConnectViewModel.uploadData() }
        binding.clear.setOnClickListener { healthConnectViewModel.clearData() }
    }

    private fun showCalendar(date: ZonedDateTime, onSelected: (ZonedDateTime) -> Unit) {
        val now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS)
        val minimumSeconds = now.minusDays(29).toEpochSecond() * 1000
        val maximumSeconds = now.minusDays(1).toEpochSecond() * 1000
        val currentSeconds = date.toEpochSecond() * 1000

        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.select_a_day))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setSelection(currentSeconds)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(minimumSeconds)
                    .setValidator(DateValidatorPointBackward.before(maximumSeconds))
                    .build()
            )
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    val selected = Instant.ofEpochMilli(it)
                        .atOffset(ZoneOffset.UTC)
                        .toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault())

                    onSelected(selected)
                }
            }
            .show(childFragmentManager, "CALENDAR")
    }
}