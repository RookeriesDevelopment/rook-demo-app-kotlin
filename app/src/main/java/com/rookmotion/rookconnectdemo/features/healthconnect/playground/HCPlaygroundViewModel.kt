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
import com.rookmotion.rook.health_connect.domain.model.event.HCNutritionEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCOxygenationEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCPhysicalEvent
import com.rookmotion.rook.health_connect.domain.model.event.HCTemperatureEvent
import com.rookmotion.rook.health_connect.domain.model.summary.HCBodySummary
import com.rookmotion.rook.health_connect.domain.model.summary.HCPhysicalSummary
import com.rookmotion.rook.health_connect.domain.model.summary.HCSleepSummary
import com.rookmotion.rook.health_connect.domain.time.UserTimeZone
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
    private val rookHealthConnectManager: RookHealthConnectManager,
) : ViewModel() {

    private val _userTimeZoneState = MutableStateFlow(UserTimeZoneState())
    val userTimeZoneState get() = _userTimeZoneState.asStateFlow()

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

    private val _bloodGlucoseEventState =
        MutableStateFlow<HealthDataState<List<HCBloodGlucoseEvent>>>(
            HealthDataState()
        )
    val bloodGlucoseEventState get() = _bloodGlucoseEventState.asStateFlow()

    private val _bloodPressureEventState =
        MutableStateFlow<HealthDataState<List<HCBloodPressureEvent>>>(
            HealthDataState()
        )
    val bloodPressureEventState get() = _bloodPressureEventState.asStateFlow()

    private val _bodyMetricsEventState =
        MutableStateFlow<HealthDataState<List<HCBodyMetricsEvent>>>(
            HealthDataState()
        )
    val bodyMetricsEventState get() = _bodyMetricsEventState.asStateFlow()

    private val _heartRateBodyEventState =
        MutableStateFlow<HealthDataState<List<HCHeartRateEvent>>>(
            HealthDataState()
        )
    val heartRateBodyEventState get() = _heartRateBodyEventState.asStateFlow()

    private val _heartRatePhysicalEventState =
        MutableStateFlow<HealthDataState<List<HCHeartRateEvent>>>(
            HealthDataState()
        )
    val heartRatePhysicalEventState get() = _heartRatePhysicalEventState.asStateFlow()

    private val _hydrationEventState = MutableStateFlow<HealthDataState<List<HCHydrationEvent>>>(
        HealthDataState()
    )
    val hydrationEventState get() = _hydrationEventState.asStateFlow()

    private val _nutritionEventState = MutableStateFlow<HealthDataState<List<HCNutritionEvent>>>(
        HealthDataState()
    )
    val nutritionEventState get() = _nutritionEventState.asStateFlow()

    private val _oxygenationBodyEventState =
        MutableStateFlow<HealthDataState<List<HCOxygenationEvent>>>(
            HealthDataState()
        )
    val oxygenationBodyEventState get() = _oxygenationBodyEventState.asStateFlow()

    private val _oxygenationPhysicalEventState =
        MutableStateFlow<HealthDataState<List<HCOxygenationEvent>>>(
            HealthDataState()
        )
    val oxygenationPhysicalEventState get() = _oxygenationPhysicalEventState.asStateFlow()

    private val _temperatureEventState =
        MutableStateFlow<HealthDataState<List<HCTemperatureEvent>>>(HealthDataState())
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
            val bloodGlucoseEventDate = getLastExtractionDate(
                HCRookDataType.BLOOD_GLUCOSE_BODY_EVENT
            )
            val bloodPressureEventDate = getLastExtractionDate(
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
            val hydrationEventDate = getLastExtractionDate(
                HCRookDataType.HYDRATION_BODY_EVENT
            )
            val moodEventDate = getLastExtractionDate(
                HCRookDataType.MOOD_BODY_EVENT
            )
            val nutritionEventDate = getLastExtractionDate(
                HCRookDataType.NUTRITION_BODY_EVENT
            )
            val oxygenationBodyEventDate = getLastExtractionDate(
                HCRookDataType.OXYGENATION_BODY_EVENT
            )
            val oxygenationPhysicalEventDate = getLastExtractionDate(
                HCRookDataType.OXYGENATION_PHYSICAL_EVENT
            )
            val stressEventDate = getLastExtractionDate(
                HCRookDataType.STRESS_PHYSICAL_EVENT
            )
            val temperatureEventDate = getLastExtractionDate(
                HCRookDataType.TEMPERATURE_BODY_EVENT
            )

            val state = LastExtractionDateState.Success(
                sleepSummaryDate = sleepSummaryDate,
                physicalSummaryDate = physicalSummaryDate,
                physicalEventDate = physicalEventDate,
                bodySummaryDate = bodySummaryDate,
                bloodGlucoseEventDate = bloodGlucoseEventDate,
                bloodPressureEventDate = bloodPressureEventDate,
                bodyMetricsEventDate = bodyMetricsEventDate,
                heartRateBodyEventDate = heartRateBodyEventDate,
                heartRatePhysicalEventDate = heartRatePhysicalEventDate,
                hydrationEventDate = hydrationEventDate,
                moodEventDate = moodEventDate,
                nutritionEventDate = nutritionEventDate,
                oxygenationBodyEventDate = oxygenationBodyEventDate,
                oxygenationPhysicalEventDate = oxygenationPhysicalEventDate,
                stressEventDate = stressEventDate,
                temperatureEventDate = temperatureEventDate,
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

    fun getUserTimeZone() {
        _userTimeZoneState.update {
            it.copy(
                extracting = true,
                extracted = null,
                extractError = null,
                uploading = false,
                uploaded = false,
                uploadError = null
            )
        }

        viewModelScope.launch {
            try {
                val extracted = rookHealthConnectManager.getUserTimeZone()

                _userTimeZoneState.update { it.copy(extracting = false, extracted = extracted) }
            } catch (e: Exception) {
                _userTimeZoneState.update {
                    it.copy(
                        extracting = false,
                        extractError = e.toString()
                    )
                }
            }
        }
    }

    fun uploadUserTimeZone(userTimeZone: UserTimeZone) {
        _userTimeZoneState.update {
            it.copy(
                uploading = true,
                uploaded = false,
                uploadError = null
            )
        }

        viewModelScope.launch {
            try {
                rookTransmissionManager.uploadUserTimeZone(
                    userTimeZone.timezone,
                    userTimeZone.offset
                )

                _userTimeZoneState.update {
                    it.copy(
                        extracted = null,
                        uploading = false,
                        uploaded = true,
                    )
                }
            } catch (e: Exception) {
                _userTimeZoneState.update { it.copy(uploading = false, uploadError = e.toString()) }
            }
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

    fun getBloodGlucoseEvent(date: ZonedDateTime) {
        _bloodGlucoseEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyBloodGlucoseEvents(date)

                _bloodGlucoseEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _bloodGlucoseEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueBloodGlucoseEvent(bloodGlucoseEvent: List<HCBloodGlucoseEvent>) {
        _bloodGlucoseEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                bloodGlucoseEvent.forEach {
                    rookTransmissionManager.enqueueBloodGlucoseEvent(it.toItem())
                }

                _bloodGlucoseEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _bloodGlucoseEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun getBloodPressureEvent(date: ZonedDateTime) {
        _bloodPressureEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyBloodPressureEvents(date)

                _bloodPressureEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _bloodPressureEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueBloodPressureEvent(bloodPressureEvent: List<HCBloodPressureEvent>) {
        _bloodPressureEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                bloodPressureEvent.forEach {
                    rookTransmissionManager.enqueueBloodPressureEvent(it.toItem())
                }

                _bloodPressureEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _bloodPressureEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun getBodyMetricsEvent(date: ZonedDateTime) {
        _bodyMetricsEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyMetricsEvents(date)

                _bodyMetricsEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _bodyMetricsEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueBodyMetricsEvent(bodyMetricsEvent: List<HCBodyMetricsEvent>) {
        _bodyMetricsEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                bodyMetricsEvent.forEach {
                    rookTransmissionManager.enqueueBodyMetricsEvent(it.toItem())
                }

                _bodyMetricsEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _bodyMetricsEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun getHeartRateBodyEvent(date: ZonedDateTime) {
        _heartRateBodyEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyHeartRateEvents(date)

                _heartRateBodyEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _heartRateBodyEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueHeartRateBodyEvent(heartRateBodyEvent: List<HCHeartRateEvent>) {
        _heartRateBodyEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                heartRateBodyEvent.forEach {
                    rookTransmissionManager.enqueueHeartRateEvent(it.toItem())
                }

                _heartRateBodyEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _heartRateBodyEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
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

    fun getHydrationEvent(date: ZonedDateTime) {
        _hydrationEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyHydrationEvents(date)

                _hydrationEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _hydrationEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueHydrationEvent(hydrationEvent: List<HCHydrationEvent>) {
        _hydrationEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                hydrationEvent.forEach {
                    rookTransmissionManager.enqueueHydrationEvent(it.toItem())
                }

                _hydrationEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _hydrationEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun getNutritionEvent(date: ZonedDateTime) {
        _nutritionEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyNutritionEvents(date)

                _nutritionEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _nutritionEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueNutritionEvent(nutritionEvent: List<HCNutritionEvent>) {
        _nutritionEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                nutritionEvent.forEach {
                    rookTransmissionManager.enqueueNutritionEvent(it.toItem())
                }

                _nutritionEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _nutritionEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun getOxygenationBodyEvent(date: ZonedDateTime) {
        _oxygenationBodyEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyOxygenationEvents(date)

                _oxygenationBodyEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _oxygenationBodyEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueOxygenationBodyEvent(oxygenationBodyEvent: List<HCOxygenationEvent>) {
        _oxygenationBodyEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                oxygenationBodyEvent.forEach {
                    rookTransmissionManager.enqueueOxygenationEvent(it.toItem())
                }

                _oxygenationBodyEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _oxygenationBodyEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun getOxygenationPhysicalEvent(date: ZonedDateTime) {
        _oxygenationPhysicalEventState.update {
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
                val extracted = rookHealthConnectManager.getPhysicalOxygenationEvents(date)

                _oxygenationPhysicalEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _oxygenationPhysicalEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueOxygenationPhysicalEvent(oxygenationPhysicalEvent: List<HCOxygenationEvent>) {
        _oxygenationPhysicalEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                oxygenationPhysicalEvent.forEach {
                    rookTransmissionManager.enqueueOxygenationEvent(it.toItem())
                }

                _oxygenationPhysicalEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _oxygenationPhysicalEventState.update {
                    it.copy(
                        enqueueing = false,
                        enqueueError = e.toString()
                    )
                }
            }
        }
    }

    fun getTemperatureEvent(date: ZonedDateTime) {
        _temperatureEventState.update {
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
                val extracted = rookHealthConnectManager.getBodyTemperatureEvents(date)

                _temperatureEventState.update {
                    it.copy(extracting = false, extracted = extracted)
                }
            } catch (e: Exception) {
                _temperatureEventState.update {
                    it.copy(extracting = false, extractError = e.toString())
                }
            }
        }
    }

    fun enqueueTemperatureEvent(temperatureEvent: List<HCTemperatureEvent>) {
        _temperatureEventState.update {
            it.copy(
                enqueueing = true,
                enqueued = false,
                enqueueError = null
            )
        }

        viewModelScope.launch {
            try {
                temperatureEvent.forEach {
                    rookTransmissionManager.enqueueTemperatureEvent(it.toItem())
                }

                _temperatureEventState.update {
                    it.copy(
                        extracted = null,
                        enqueueing = false,
                        enqueued = true,
                    )
                }
            } catch (e: Exception) {
                _temperatureEventState.update {
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
                rookTransmissionManager.uploadSleepSummaries()
                rookTransmissionManager.uploadPhysicalSummaries()
                rookTransmissionManager.uploadPhysicalEvents()
                rookTransmissionManager.uploadBodySummaries()
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
