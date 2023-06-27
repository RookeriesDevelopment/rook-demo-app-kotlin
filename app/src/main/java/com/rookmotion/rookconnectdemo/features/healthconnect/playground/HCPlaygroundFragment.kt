package com.rookmotion.rookconnectdemo.features.healthconnect.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.rookmotion.rookconnectdemo.R
import com.rookmotion.rookconnectdemo.databinding.FragmentHcPlaygroundBinding
import com.rookmotion.rookconnectdemo.di.ViewModelFactory
import com.rookmotion.rookconnectdemo.extension.repeatOnResume
import com.rookmotion.rookconnectdemo.extension.serviceLocator
import com.rookmotion.rookconnectdemo.extension.snackLong
import com.rookmotion.rookconnectdemo.extension.snackShort
import com.rookmotion.rookconnectdemo.extension.toUTCSameInstant
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class HCPlaygroundFragment : Fragment() {

    private var _binding: FragmentHcPlaygroundBinding? = null
    private val binding get() = _binding!!

    private val hcPlaygroundViewModel by viewModels<HCPlaygroundViewModel> {
        ViewModelFactory(serviceLocator)
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
        hcPlaygroundViewModel.getDataLastDate()
    }

    private fun initListeners() {
        repeatOnResume {
            hcPlaygroundViewModel.dataLastDate.collect {
                when (it) {
                    LastExtractionDateState.None -> {
                        binding.sleepSummaryDate.isVisible = false
                        binding.physicalSummaryDate.isVisible = false
                        binding.physicalEventDate.isVisible = false
                        binding.bodySummaryDate.isVisible = false
                        binding.heartRatePhysicalEvent.isVisible = false
                    }

                    is LastExtractionDateState.Error -> binding.root.snackLong(
                        message = it.message,
                        action = getString(R.string.retry),
                        onClick = {
                            hcPlaygroundViewModel.getDataLastDate()
                        },
                    )

                    is LastExtractionDateState.Success -> {
                        binding.sleepSummaryDate.setOnClickListener { _ ->
                            showCalendar(it.sleepSummaryDate) { date ->
                                hcPlaygroundViewModel.getSleep(date.toUTCSameInstant())
                            }
                        }

                        binding.physicalSummaryDate.setOnClickListener { _ ->
                            showCalendar(it.physicalSummaryDate) { date ->
                                hcPlaygroundViewModel.getPhysical(date.toUTCSameInstant())
                            }
                        }

                        binding.physicalEventDate.setOnClickListener { _ ->
                            showCalendar(it.physicalEventDate) { date ->
                                hcPlaygroundViewModel.getPhysicalEvent(date.toUTCSameInstant())
                            }
                        }

                        binding.bodySummaryDate.setOnClickListener { _ ->
                            showCalendar(it.bodySummaryDate) { date ->
                                hcPlaygroundViewModel.getBody(date.toUTCSameInstant())
                            }
                        }

                        binding.heartRatePhysicalEventDate.setOnClickListener { _ ->
                            showCalendar(it.heartRatePhysicalEventDate) { date ->
                                hcPlaygroundViewModel.getHeartRatePhysicalEvent(date.toUTCSameInstant())
                            }
                        }

                        binding.sleepSummaryDate.isVisible = true
                        binding.physicalSummaryDate.isVisible = true
                        binding.physicalEventDate.isVisible = true
                        binding.bodySummaryDate.isVisible = true
                        binding.heartRatePhysicalEvent.isVisible = true
                    }
                }
            }
        }

        repeatOnResume {
            hcPlaygroundViewModel.sleepState.collect {
                binding.sleepSummaryDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.sleepSummary.text = it.extracted.toString()

                    binding.enqueueSleep.setOnClickListener { _ ->
                        hcPlaygroundViewModel.enqueueSleep(it.extracted)
                    }
                } else {
                    binding.sleepSummary.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueueSleep.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued) {
                    binding.root.snackShort(getString(R.string.health_data_enqueued))
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            hcPlaygroundViewModel.physicalState.collect {
                binding.physicalSummaryDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.physicalSummary.text = it.extracted.toString()

                    binding.enqueuePhysical.setOnClickListener { _ ->
                        hcPlaygroundViewModel.enqueuePhysical(it.extracted)
                    }
                } else {
                    binding.physicalSummary.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueuePhysical.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued) {
                    binding.root.snackShort(getString(R.string.health_data_enqueued))
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            hcPlaygroundViewModel.physicalEventState.collect {
                binding.physicalEventDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.physicalEvent.text = it.extracted.toString()

                    binding.enqueuePhysicalEvent.setOnClickListener { _ ->
                        hcPlaygroundViewModel.enqueuePhysicalEvents(it.extracted)
                    }
                } else {
                    binding.physicalEvent.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueuePhysicalEvent.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued) {
                    binding.root.snackShort(getString(R.string.health_data_enqueued))
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            hcPlaygroundViewModel.bodyState.collect {
                binding.bodySummaryDate.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.bodySummary.text = it.extracted.toString()

                    binding.enqueueBody.setOnClickListener { _ ->
                        hcPlaygroundViewModel.enqueueBody(it.extracted)
                    }
                } else {
                    binding.bodySummary.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueueBody.isEnabled = (!it.enqueueing && it.extracted != null)

                if (it.enqueued) {
                    binding.root.snackShort(getString(R.string.health_data_enqueued))
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            hcPlaygroundViewModel.heartRatePhysicalState.collect {
                binding.heartRatePhysicalEvent.isEnabled = !it.extracting

                if (it.extracted != null) {
                    binding.heartRatePhysicalEvent.text = it.extracted.joinToString("\n\n")

                    binding.enqueueHeartRatePhysicalEvent.setOnClickListener { _ ->
                        hcPlaygroundViewModel.enqueueHeartRatePhysicalEvent(it.extracted)
                    }
                } else {
                    binding.heartRatePhysicalEvent.text = ""
                }

                if (it.extractError != null) {
                    binding.root.snackShort(it.extractError)
                }

                binding.enqueueHeartRatePhysicalEvent.isEnabled =
                    (!it.enqueueing && it.extracted != null)

                if (it.enqueued) {
                    binding.root.snackShort(getString(R.string.health_data_enqueued))
                }

                if (it.enqueueError != null) {
                    binding.root.snackShort(it.enqueueError)
                }
            }
        }

        repeatOnResume {
            hcPlaygroundViewModel.uploadState.collect {
                when (it) {
                    is UploadState.Ready -> {
                        // Ignored
                    }

                    UploadState.Uploading -> {
                        binding.upload.setText(R.string.uploading)
                        binding.upload.isEnabled = false
                    }

                    is UploadState.Error -> {
                        binding.root.snackLong(it.message, getString(R.string.retry)) {
                            hcPlaygroundViewModel.uploadData()
                        }
                        binding.upload.isEnabled = true
                    }

                    UploadState.Uploaded -> {
                        binding.upload.setText(R.string.upload_queued)
                        binding.upload.isEnabled = true
                    }
                }
            }
        }

        repeatOnResume {
            hcPlaygroundViewModel.clearQueueState.collect {
                when (it) {
                    ClearState.Clearing -> {
                        binding.clear.setText(R.string.clearing)
                        binding.clear.isEnabled = false
                    }

                    is ClearState.Error -> {
                        binding.root.snackLong(it.message, getString(R.string.retry)) {
                            hcPlaygroundViewModel.clearData()
                        }
                        binding.clear.isEnabled = true
                    }

                    ClearState.Cleared -> {
                        binding.clear.setText(R.string.clear_queued)
                        binding.clear.isEnabled = true
                    }
                }
            }
        }
    }

    private fun initWidgets() {
        binding.upload.setOnClickListener { hcPlaygroundViewModel.uploadData() }
        binding.clear.setOnClickListener { hcPlaygroundViewModel.clearData() }
    }

    private fun showCalendar(date: ZonedDateTime, onSelected: (ZonedDateTime) -> Unit) {
        val now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS)
        val minimumSeconds = now.minusDays(30).toEpochSecond() * 1000
        val maximumSeconds = now.minusDays(1).toEpochSecond() * 1000
        val currentSeconds = date.toEpochSecond() * 1000

        val dateValidators = listOf(
            DateValidatorPointForward.from(minimumSeconds),
            DateValidatorPointBackward.before(maximumSeconds),
        )

        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.select_a_day))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setSelection(currentSeconds)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(minimumSeconds)
                    .setEnd(maximumSeconds)
                    .setValidator(CompositeDateValidator.allOf(dateValidators))
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