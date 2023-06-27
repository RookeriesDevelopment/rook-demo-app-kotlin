package com.rookmotion.rookconnectdemo.features.healthconnect.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.health_connect.RookHealthConnectManager
import com.rookmotion.rook.health_connect.domain.enums.HCRookDataType
import com.rookmotion.rook.health_connect.domain.exception.DateNotFoundException
import com.rookmotion.rook.health_connect.domain.model.event.HCBloodGlucoseEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCBloodPressureEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCBodyMetricsEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCHeartRateEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCHydrationEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCMoodEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCNutritionEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCOxygenationEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCPhysicalEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCStressEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCTemperatureEvent
import com.rookmotion.rook.health_connect.domain.model.summary.HCBodySummary
import com.rookmotion.rook.health_connect.domain.model.summary.HCPhysicalSummary
import com.rookmotion.rook.health_connect.domain.model.summary.HCSleepSummary
import com.rookmotion.rook.transmission.RookTransmissionManager
import com.rookmotion.rookconnectdemo.extension.atStartOfDayUTC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZonedDateTime

class HCPlaygroundViewModel(
    private val rookTransmissionManager: RookTransmissionManager,
    private val rookHealthConnectManager: RookHealthConnectManager
) : ViewModel() {

    private val _dataLastDate = MutableStateFlow<LastExtractionDateState>(
        LastExtractionDateState.None
    )
    val dataLastDate get() = _dataLastDate.asStateFlow()

    private val _sleepState = MutableStateFlow<HealthDataState<HCSleepSummary>>(HealthDataState())
    val sleepState get() = _sleepState.asStateFlow()

    private val _physicalState = MutableStateFlow<HealthDataState<HCPhysicalSummary>>(
        HealthDataState()
    )
    val physicalState get() = _physicalState.asStateFlow()

    private val _physicalEventState = MutableStateFlow<HealthDataState<List<HCPhysicalEvent>>>(
        HealthDataState()
    )
    val physicalEventState get() = _physicalEventState.asStateFlow()

    private val _bodyState = MutableStateFlow<HealthDataState<HCBodySummary>>(HealthDataState())
    val bodyState get() = _bodyState.asStateFlow()

    private val _bloodGlucoseEventState = MutableStateFlow<HealthDataState<HCBloodGlucoseEvent>>(
        HealthDataState()
    )
    val bloodGlucoseEventState get() = _bloodGlucoseEventState.asStateFlow()

    private val _bloodPressureEventState = MutableStateFlow<HealthDataState<HCBloodPressureEvent>>(
        HealthDataState()
    )
    val bloodPressureEventState get() = _bloodPressureEventState.asStateFlow()

    private val _bodyMetricsEventState = MutableStateFlow<HealthDataState<HCBodyMetricsEvent>>(
        HealthDataState()
    )
    val bodyMetricsEventState get() = _bodyMetricsEventState.asStateFlow()

    private val _heartRateBodyEventState = MutableStateFlow<HealthDataState<HCHeartRateEvent>>(
        HealthDataState()
    )
    val heartRateBodyEventState get() = _heartRateBodyEventState.asStateFlow()

    private val _heartRatePhysicalEventState =
        MutableStateFlow<HealthDataState<List<HCHeartRateEvent>>>(
            HealthDataState()
        )
    val heartRatePhysicalEventState get() = _heartRatePhysicalEventState.asStateFlow()

    private val _hydrationEventState = MutableStateFlow<HealthDataState<HCHydrationEvent>>(
        HealthDataState()
    )
    val hydrationEventState get() = _hydrationEventState.asStateFlow()

    private val _moodEventState = MutableStateFlow<HealthDataState<HCMoodEvent>>(
        HealthDataState()
    )
    val moodEventState get() = _moodEventState.asStateFlow()

    private val _nutritionEventState = MutableStateFlow<HealthDataState<HCNutritionEvent>>(
        HealthDataState()
    )
    val nutritionEventState get() = _nutritionEventState.asStateFlow()

    private val _oxygenationBodyEventState = MutableStateFlow<HealthDataState<HCOxygenationEvent>>(
        HealthDataState()
    )
    val oxygenationBodyEventState get() = _oxygenationBodyEventState.asStateFlow()

    private val _oxygenationPhysicalEventState =
        MutableStateFlow<HealthDataState<List<HCOxygenationEvent>>>(
            HealthDataState()
        )
    val oxygenationPhysicalEventState get() = _oxygenationPhysicalEventState.asStateFlow()

    private val _stressEventState = MutableStateFlow<HealthDataState<List<HCStressEvent>>>(
        HealthDataState()
    )
    val stressEventState get() = _stressEventState.asStateFlow()

    private val _temperatureEventState = MutableStateFlow<HealthDataState<HCTemperatureEvent>>(
        HealthDataState()
    )
    val temperatureEventState get() = _temperatureEventState.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Ready)
    val uploadState get() = _uploadState.asStateFlow()

    private val _clearQueueState = MutableStateFlow<ClearState>(ClearState.Cleared)
    val clearQueueState get() = _clearQueueState.asStateFlow()

    fun getDataLastDate() {
        viewModelScope.launch {
            val sleepSummaryDate = getLastExtractionDate(HCRookDataType.SLEEP_SUMMARY)
            val physicalSummaryDate = getLastExtractionDate(HCRookDataType.PHYSICAL_SUMMARY)
            val physicalEventDate = getLastExtractionDate(HCRookDataType.PHYSICAL_EVENT)
            val bodySummaryDate = getLastExtractionDate(HCRookDataType.BODY_SUMMARY)
            val bloodGlucoseBodyEventDate = getLastExtractionDate(
                HCRookDataType.BLOOD_GLUCOSE_BODY_EVENT
            )
            val bloodPressureBodyEventDate = getLastExtractionDate(
                HCRookDataType.BLOOD_PRESSURE_BODY_EVENT
            )
            val bodyMetricsEventDate = getLastExtractionDate(
                HCRookDataType.BODY_METRICS_EVENT
            )
            val heartRateBodyEventDate = getLastExtractionDate(
                HCRookDataType.HEART_RATE_BODY_EVENT
            )
            val heartRatePhysicalEventDate = getLastExtractionDate(
                HCRookDataType.HEART_RATE_PHYSICAL_EVENT
            )
            val hydrationBodyDate = getLastExtractionDate(
                HCRookDataType.HYDRATION_BODY_EVENT
            )
            val moodBodyDate = getLastExtractionDate(
                HCRookDataType.MOOD_BODY_EVENT
            )
            val nutritionBodyEventDate = getLastExtractionDate(
                HCRookDataType.NUTRITION_BODY_EVENT
            )
            val oxygenationBodyEventDate = getLastExtractionDate(
                HCRookDataType.OXYGENATION_BODY_EVENT
            )
            val oxygenationPhysicalEventDate = getLastExtractionDate(
                HCRookDataType.OXYGENATION_PHYSICAL_EVENT
            )
            val stressPhysicalEventDate = getLastExtractionDate(
                HCRookDataType.STRESS_PHYSICAL_EVENT
            )
            val temperatureBodyEventDate = getLastExtractionDate(
                HCRookDataType.TEMPERATURE_BODY_EVENT
            )

            val state = LastExtractionDateState.Success(
                sleepSummaryDate = sleepSummaryDate,
                physicalSummaryDate = physicalSummaryDate,
                physicalEventDate = physicalEventDate,
                bodySummaryDate = bodySummaryDate,
                bloodGlucoseBodyEventDate = bloodGlucoseBodyEventDate,
                bloodPressureBodyEventDate = bloodPressureBodyEventDate,
                bodyMetricsEventDate = bodyMetricsEventDate,
                heartRateBodyEventDate = heartRateBodyEventDate,
                heartRatePhysicalEventDate = heartRatePhysicalEventDate,
                hydrationBodyDate = hydrationBodyDate,
                moodBodyDate = moodBodyDate,
                nutritionBodyEventDate = nutritionBodyEventDate,
                oxygenationBodyEventDate = oxygenationBodyEventDate,
                oxygenationPhysicalEventDate = oxygenationPhysicalEventDate,
                stressPhysicalEventDate = stressPhysicalEventDate,
                temperatureBodyEventDate = temperatureBodyEventDate,
            )

            _dataLastDate.emit(state)
        }
    }

    private fun getLastExtractionDate(hcRookDataType: HCRookDataType): ZonedDateTime {
        return try {
            rookHealthConnectManager.getLastExtractionDate(hcRookDataType)
        } catch (e: DateNotFoundException) {
            LocalDate.now().minusDays(1).atStartOfDayUTC()
        }
    }

    fun getSleep(date: ZonedDateTime) {
        _sleepState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                enqueueing = false,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                val extracted = rookHealthConnectManager.getSleepSummary(date)

                _sleepState.update { it.copy(extracting = false, extracted = extracted) }
            } catch (e: Exception) {
                _sleepState.update { it.copy(extracting = false, extractError = e.toString()) }
            }
        }
    }

    fun enqueueSleep(sleepSummary: HCSleepSummary) {
        _sleepState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                rookTransmissionManager.enqueueSleepSummary(sleepSummary.toItem())

                _sleepState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
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
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                val extracted = rookHealthConnectManager.getPhysicalSummary(date)

                _physicalState.update { it.copy(extracting = false, extracted = extracted) }
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
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                rookTransmissionManager.enqueuePhysicalSummary(physicalSummary.toItem())

                _physicalState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
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

    fun getPhysicalEvent(date: ZonedDateTime) {
        _physicalEventState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                enqueueing = false,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                val extracted = rookHealthConnectManager.getPhysicalEvents(date)

                _physicalEventState.update { it.copy(extracting = false, extracted = extracted) }
            } catch (e: Exception) {
                _physicalEventState.update {
                    it.copy(
                        extracting = false,
                        extractError = e.toString()
                    )
                }
            }
        }
    }

    fun enqueuePhysicalEvents(physicalEvent: List<HCPhysicalEvent>) {
        _physicalEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                physicalEvent.forEach {
                    rookTransmissionManager.enqueuePhysicalEvent(it.toItem())
                }

                _physicalEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _physicalEventState.update {
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
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                val extracted = rookHealthConnectManager.getBodySummary(date)

                _bodyState.update { it.copy(extracting = false, extracted = extracted) }
            } catch (e: Exception) {
                _bodyState.update { it.copy(extracting = false, extractError = e.toString()) }
            }
        }
    }

    fun enqueueBody(bodySummary: HCBodySummary) {
        _bodyState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                rookTransmissionManager.enqueueBodySummary(bodySummary.toItem())

                _bodyState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _bodyState.update { it.copy(enqueueing = false, enqueueError = e.toString()) }
            }
        }
    }

    fun getHeartRatePhysicalEvent(date: ZonedDateTime) {
        _heartRatePhysicalEventState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                enqueueing = false,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                val extracted = rookHealthConnectManager.getPhysicalHeartRateEvents(date)

                _heartRatePhysicalEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _heartRatePhysicalEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueHeartRatePhysicalEvent(heartRatePhysicalEvent: List<HCHeartRateEvent>) {
        _heartRatePhysicalEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                heartRatePhysicalEvent.forEach {
                    rookTransmissionManager.enqueueHeartRateEvent(it.toItem())
                }

                _heartRatePhysicalEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _heartRatePhysicalEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun uploadData() {
        viewModelScope.launch {
            _uploadState.emit(UploadState.Uploading)

            try {
                rookTransmissionManager.uploadAll()
                rookTransmissionManager.uploadBloodGlucoseEvents()
                rookTransmissionManager.uploadBloodPressureEvents()
                rookTransmissionManager.uploadBodyMetricsEvents()
                rookTransmissionManager.uploadHeartRateEvents()
                rookTransmissionManager.uploadHydrationEvents()
                rookTransmissionManager.uploadMoodEvents()
                rookTransmissionManager.uploadNutritionEvents()
                rookTransmissionManager.uploadOxygenationEvents()
                rookTransmissionManager.uploadStressEvents()
                rookTransmissionManager.uploadTemperatureEvents()

                _uploadState.emit(UploadState.Uploaded)
            } catch (e: Exception) {
                _uploadState.emit(UploadState.Error(e.toString()))
            }
        }
    }

    fun clearData() {
        viewModelScope.launch {
            _clearQueueState.emit(ClearState.Clearing)

            try {
                rookTransmissionManager.clearQueuedSleepSummaries()
                rookTransmissionManager.clearQueuedPhysicalSummaries()
                rookTransmissionManager.clearQueuedPhysicalEvents()
                rookTransmissionManager.clearQueuedBodySummaries()
                rookTransmissionManager.clearQueuedBloodGlucoseEvents()
                rookTransmissionManager.clearQueuedBloodPressureEvents()
                rookTransmissionManager.clearQueuedBodyMetricsEvents()
                rookTransmissionManager.clearQueuedHeartRateEvents()
                rookTransmissionManager.clearQueuedHydrationEvents()
                rookTransmissionManager.clearQueuedMoodEvents()
                rookTransmissionManager.clearQueuedNutritionEvents()
                rookTransmissionManager.clearQueuedOxygenationEvents()
                rookTransmissionManager.clearQueuedStressEvents()
                rookTransmissionManager.clearQueuedTemperatureEvents()

                _clearQueueState.emit(ClearState.Cleared)
            } catch (e: Exception) {
                _clearQueueState.emit(ClearState.Error(e.toString()))
            }
        }
    }
}
