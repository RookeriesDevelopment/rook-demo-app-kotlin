package com.rookmotion.rookconnectdemo.ui.health_connect.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.health_connect.RookHealthConnectManager
import com.rookmotion.rook.health_connect.domain.enums.HCRookDataType
import com.rookmotion.rook.health_connect.domain.model.HCBodySummary
import com.rookmotion.rook.health_connect.domain.model.HCPhysicalEvents
import com.rookmotion.rook.health_connect.domain.model.HCPhysicalSummary
import com.rookmotion.rook.health_connect.domain.model.HCSleepSummary
import com.rookmotion.rook.transmission.RookTransmissionManager
import com.rookmotion.rookconnectdemo.ui.common.BasicState
import com.rookmotion.rookconnectdemo.ui.common.DataState
import com.rookmotion.rookconnectdemo.ui.health_connect.playground.toItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class HCPlaygroundViewModel(
    private val transmission: RookTransmissionManager,
    private val manager: RookHealthConnectManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _dataLastDate = MutableStateFlow<DataState<HCLastDate>>(DataState.None)
    val dataLastDate get() = _dataLastDate.asStateFlow()

    private val _sleepState = MutableStateFlow<HCDataState<HCSleepSummary>>(HCDataState())
    val sleepState get() = _sleepState.asStateFlow()

    private val _physicalState = MutableStateFlow<HCDataState<HCPhysicalSummary>>(HCDataState())
    val physicalState get() = _physicalState.asStateFlow()

    private val _physicalEventsState =
        MutableStateFlow<HCDataState<HCPhysicalEvents>>(HCDataState())
    val physicalEventsState get() = _physicalEventsState.asStateFlow()

    private val _bodyState = MutableStateFlow<HCDataState<HCBodySummary>>(HCDataState())
    val bodyState get() = _bodyState.asStateFlow()

    private val _clearQueueState = MutableStateFlow<BasicState>(BasicState.None)
    val clearQueueState get() = _clearQueueState.asStateFlow()

    private val _uploadState = MutableStateFlow<BasicState>(BasicState.None)
    val uploadState get() = _uploadState.asStateFlow()

    fun getDataLastDate() {
        _dataLastDate.tryEmit(DataState.Loading)

        val sleepSummaryDate = getLastDateOrYesterday {
            manager.getLastExtractionDate(HCRookDataType.SLEEP_SUMMARY)
        }
        val physicalSummaryDate = getLastDateOrYesterday {
            manager.getLastExtractionDate(HCRookDataType.PHYSICAL_SUMMARY)
        }
        val physicalEventsDate = getLastDateOrYesterday {
            manager.getLastExtractionDate(HCRookDataType.PHYSICAL_EVENT)
        }
        val bodySummaryDate = getLastDateOrYesterday {
            manager.getLastExtractionDate(HCRookDataType.BODY_SUMMARY)
        }

        _dataLastDate.tryEmit(
            DataState.Success(
                HCLastDate(
                    sleepSummaryDate,
                    physicalSummaryDate,
                    physicalEventsDate,
                    bodySummaryDate,
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
                val result = manager.getSleepSummary(date)

                _sleepState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                _sleepState.update { it.copy(extracting = false, extractError = e.toString()) }
            }
        }
    }

    fun enqueueSleep(sleepSummary: HCSleepSummary) {
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
                _sleepState.update { it.copy(enqueueing = false, enqueueError = e.toString()) }
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
                val result = manager.getPhysicalSummary(date)

                _physicalState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                _physicalState.update {
                    it.copy(
                        extracting = false,
                        extractError = e.toString()
                    )
                }
            }
        }
    }

    fun enqueuePhysical(physicalSummary: HCPhysicalSummary) {
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
                _physicalState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
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
                val result = manager.getPhysicalEvents(date)

                _physicalEventsState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                _physicalEventsState.update {
                    it.copy(
                        extracting = false,
                        extractError = e.toString()
                    )
                }
            }
        }
    }

    fun enqueuePhysicalEvents(physicalEvents: HCPhysicalEvents) {
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
                _physicalEventsState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
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
                val result = manager.getBodySummary(date)

                _bodyState.update { it.copy(extracting = false, extracted = result) }
            } catch (e: Exception) {
                _bodyState.update { it.copy(extracting = false, extractError = e.toString()) }
            }
        }
    }

    fun enqueueBody(bodySummary: HCBodySummary) {
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
                _bodyState.update { it.copy(enqueueing = false, enqueueError = e.toString()) }
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
                _uploadState.emit(BasicState.Error(e.toString()))
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
                _clearQueueState.emit(BasicState.Error(e.toString()))
            }
        }
    }
}