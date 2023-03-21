package com.rookmotion.rookconnectdemo.home.health_connect

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.health_connect.RookHealthConnectManager
import com.rookmotion.rook.health_connect.domain.enums.AvailabilityStatus
import com.rookmotion.rook.health_connect.domain.model.BodySummary
import com.rookmotion.rook.health_connect.domain.model.PhysicalEvents
import com.rookmotion.rook.health_connect.domain.model.PhysicalSummary
import com.rookmotion.rook.health_connect.domain.model.SleepSummary
import com.rookmotion.rook.transmission.RookTransmissionManager
import com.rookmotion.rookconnectdemo.home.common.BasicState
import com.rookmotion.rookconnectdemo.home.common.DataState
import com.rookmotion.rookconnectdemo.utils.toItem
import com.rookmotion.rookconnectdemo.utils.toItems
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class HealthConnectViewModel(
    private val transmission: RookTransmissionManager,
    private val healthConnect: RookHealthConnectManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _isAvailable = MutableStateFlow<DataState<AvailabilityStatus>>(DataState.None)
    val isAvailable get() = _isAvailable.asStateFlow()

    private val _hasPermissions = MutableStateFlow<DataState<Boolean>>(DataState.None)
    val hasPermissions get() = _hasPermissions.asStateFlow()

    private val _dataLastDate = MutableStateFlow<DataState<HCLastDateState>>(DataState.None)
    val dataLastDate get() = _dataLastDate.asStateFlow()

    private val _sleepState = MutableStateFlow<HCDataState<SleepSummary>>(HCDataState())
    val sleepState get() = _sleepState.asStateFlow()

    private val _physicalState = MutableStateFlow<HCDataState<PhysicalSummary>>(HCDataState())
    val physicalState get() = _physicalState.asStateFlow()

    private val _physicalEventsState = MutableStateFlow<HCDataState<PhysicalEvents>>(HCDataState())
    val physicalEventsState get() = _physicalEventsState.asStateFlow()

    private val _bodyState = MutableStateFlow<HCDataState<BodySummary>>(HCDataState())
    val bodyState get() = _bodyState.asStateFlow()

    private val _clearQueueState = MutableStateFlow<BasicState>(BasicState.None)
    val clearQueueState get() = _clearQueueState.asStateFlow()

    private val _uploadState = MutableStateFlow<BasicState>(BasicState.None)
    val uploadState get() = _uploadState.asStateFlow()

    fun checkAvailability() {
        _isAvailable.tryEmit(DataState.Loading)

        try {
            val result = healthConnect.checkAvailability()

            _isAvailable.tryEmit(DataState.Success(result))
        } catch (e: Exception) {
            _isAvailable.tryEmit(DataState.Error(e.toString()))
        }
    }

    fun openHealthConnectSettings() {
        healthConnect.openHealthConnectSettings()
    }

    fun checkPermissions() {
        _hasPermissions.tryEmit(DataState.Loading)

        viewModelScope.launch(dispatcher) {
            try {
                val result = healthConnect.hasAllPermissions()

                _hasPermissions.emit(DataState.Success(result))
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _hasPermissions.emit(DataState.Error(e.toString()))
                }
            }
        }
    }

    fun requestPermissions(activity: Activity) {
        healthConnect.requestAllPermissions(activity)
    }

    fun getDataLastDate() {
        _dataLastDate.tryEmit(DataState.Loading)

        val sleepSummaryDate = getLastDateOrYesterday { healthConnect.getSleepSummaryLastDate() }
        val physicalSummaryDate = getLastDateOrYesterday {
            healthConnect.getPhysicalSummaryLastDate()
        }
        val physicalEventsDate = getLastDateOrYesterday {
            healthConnect.getPhysicalEventsLastDate()
        }
        val bodySummaryDate = getLastDateOrYesterday { healthConnect.getBodySummaryLastDate() }

        _dataLastDate.tryEmit(
            DataState.Success(
                HCLastDateState(
                    sleepSummaryDate,
                    physicalSummaryDate,
                    physicalEventsDate,
                    bodySummaryDate
                )
            )
        )
    }

    private fun getLastDateOrYesterday(block: () -> ZonedDateTime): ZonedDateTime {
        return try {
            block()
        } catch (e: Exception) {
            LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault())
        }
    }

    fun getSleep(date: ZonedDateTime) {
        _sleepState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                enqueueing = false,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val result = healthConnect.getSleepSummary(date)

                _sleepState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _sleepState.update { it.copy(extracting = false, extractError = e.toString()) }
                }
            }
        }
    }

    fun enqueueSleep(sleepSummary: SleepSummary) {
        _sleepState.update {
            it.copy(
                enqueueing = true,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val result = transmission.enqueueSleepSummary(sleepSummary.toItem())

                _sleepState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = result
                    )
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _sleepState.update { it.copy(enqueueing = false, enqueueError = e.toString()) }
                }
            }
        }
    }

    fun getPhysical(date: ZonedDateTime) {
        _physicalState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                enqueueing = false,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val result = healthConnect.getPhysicalSummary(date)

                _physicalState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _physicalState.update {
                        it.copy(
                            extracting = false,
                            extractError = e.toString()
                        )
                    }
                }
            }
        }
    }

    fun enqueuePhysical(physicalSummary: PhysicalSummary) {
        _physicalState.update {
            it.copy(
                enqueueing = true,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val result = transmission.enqueuePhysicalSummary(physicalSummary.toItem())

                _physicalState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = result
                    )
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _physicalState.update {
                        it.copy(
                            enqueueing = false,
                            enqueueError = e.toString()
                        )
                    }
                }
            }
        }
    }

    fun getPhysicalEvents(date: ZonedDateTime) {
        _physicalEventsState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                enqueueing = false,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val result = healthConnect.getPhysicalEvents(date)

                _physicalEventsState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _physicalEventsState.update {
                        it.copy(
                            extracting = false,
                            extractError = e.toString()
                        )
                    }
                }
            }
        }
    }

    fun enqueuePhysicalEvents(physicalEvents: PhysicalEvents) {
        _physicalEventsState.update {
            it.copy(
                enqueueing = true,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val results = mutableListOf<Boolean>()

                for (event in physicalEvents.toItems()) {
                    results.add(transmission.enqueuePhysicalEvent(event))
                }

                _physicalEventsState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = results.sumOf { success ->
                            if (success) 1L else 0L
                        }.toInt() == physicalEvents.events.size
                    )
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _physicalEventsState.update {
                        it.copy(
                            enqueueing = false,
                            enqueueError = e.toString()
                        )
                    }
                }
            }
        }
    }

    fun getBody(date: ZonedDateTime) {
        _bodyState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                enqueueing = false,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val result = healthConnect.getBodySummary(date)

                _bodyState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _bodyState.update { it.copy(extracting = false, extractError = e.toString()) }
                }
            }
        }
    }

    fun enqueueBody(bodySummary: BodySummary) {
        _bodyState.update {
            it.copy(
                enqueueing = true,
                enqueued = null,
                enqueueError = null
            )
        }

        viewModelScope.launch(dispatcher) {
            try {
                val result = transmission.enqueueBodySummary(bodySummary.toItem())

                _bodyState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = result
                    )
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _bodyState.update { it.copy(enqueueing = false, enqueueError = e.toString()) }
                }
            }
        }
    }

    fun uploadData() {
        _uploadState.tryEmit(BasicState.Loading)
        viewModelScope.launch(dispatcher) {
            try {
                transmission.uploadAll()

                _uploadState.emit(BasicState.Success)
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _uploadState.emit(BasicState.Error(e.toString()))
                }
            }
        }
    }

    fun clearData() {
        _clearQueueState.tryEmit(BasicState.Loading)

        viewModelScope.launch(dispatcher) {
            try {
                transmission.clearQueuedSleepSummaries()
                transmission.clearQueuedPhysicalSummaries()
                transmission.clearQueuedPhysicalEvents()
                transmission.clearQueuedBodySummaries()

                _clearQueueState.emit(BasicState.Success)
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                } else {
                    _clearQueueState.emit(BasicState.Error(e.toString()))
                }
            }
        }
    }
}