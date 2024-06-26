package com.rookmotion.rookconnectdemo.features.sdkplayground

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookEventManager
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.RookHelpers
import com.rookmotion.rook.sdk.RookSummaryManager
import com.rookmotion.rook.sdk.domain.enums.HealthConnectAvailability
import com.rookmotion.rook.sdk.domain.enums.HealthDataType
import com.rookmotion.rook.sdk.domain.enums.SyncStatus
import com.rookmotion.rook.sdk.domain.exception.DeviceNotSupportedException
import com.rookmotion.rook.sdk.domain.exception.HealthConnectNotInstalledException
import com.rookmotion.rook.sdk.domain.exception.HttpRequestException
import com.rookmotion.rook.sdk.domain.exception.MissingPermissionsException
import com.rookmotion.rook.sdk.domain.exception.RequestQuotaExceededException
import com.rookmotion.rook.sdk.domain.exception.SDKNotAuthorizedException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.TimeoutException
import com.rookmotion.rook.sdk.domain.exception.UserNotInitializedException
import com.rookmotion.rook.sdk.domain.model.SyncStatusWithData
import com.rookmotion.rookconnectdemo.extension.appendConsoleLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

class SDKPlaygroundViewModel(
    private val rookHealthPermissionsManager: RookHealthPermissionsManager,
    private val rookSummaryManager: RookSummaryManager,
    private val rookEventManager: RookEventManager,
) : ViewModel() {

    private val _availability = MutableStateFlow("")
    val availability get() = _availability.asStateFlow()

    private val _permissions = MutableStateFlow("")
    val permissions get() = _permissions.asStateFlow()

    private val _syncHealthData = MutableStateFlow("")
    val syncHealthData get() = _syncHealthData.asStateFlow()

    private val _pendingSummaries = MutableStateFlow("")
    val pendingSummaries get() = _pendingSummaries.asStateFlow()

    private val _pendingEvents = MutableStateFlow("")
    val pendingEvents get() = _pendingEvents.asStateFlow()

    fun checkAvailability(context: Context) {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Checking availability...")
            _availability.emit(stringBuilder.toString())

            val string = when (RookHealthPermissionsManager.checkAvailability(context)) {
                HealthConnectAvailability.INSTALLED -> "Health Connect is installed! You can skip the next step"
                HealthConnectAvailability.NOT_INSTALLED -> "Health Connect is not installed. Please download from the Play Store"
                else -> "This device is not compatible with health connect. Please close the app"
            }

            stringBuilder.appendConsoleLine("Availability checked successfully")
            stringBuilder.appendConsoleLine(string)
            _availability.emit(stringBuilder.toString())
        }
    }

    fun checkPermissions() {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Checking all permissions (Sleep, Physical and Body)...")
            _permissions.emit(stringBuilder.toString())

            val result = rookHealthPermissionsManager.checkPermissions()

            result.fold(
                {
                    val string = if (it) {
                        "All permissions are granted! You can skip the next 2 steps"
                    } else {
                        "There are missing permissions. Please grant them"
                    }

                    stringBuilder.appendConsoleLine("All permissions checked successfully")
                    stringBuilder.appendConsoleLine(string)
                    _permissions.emit(stringBuilder.toString())
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                        is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                        is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                        else -> "${it.message}"
                    }

                    stringBuilder.appendConsoleLine("Error checking all permissions:")
                    stringBuilder.appendConsoleLine(error)
                    _permissions.emit(stringBuilder.toString())
                }
            )
        }
    }

    fun openHealthConnect() {
        viewModelScope.launch {
            Timber.i("Opening Health Connect...")

            val result = rookHealthPermissionsManager.openHealthConnectSettings()

            result.fold(
                {
                    Timber.i("Health Connect was opened")
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                        is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                        is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                        else -> "${it.message}"
                    }

                    Timber.e("Error opening Health Connect:")
                    Timber.e(error)
                }
            )
        }
    }

    fun syncHealthData() {
        val stringBuilder = StringBuilder()

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Syncing health data...")
            _syncHealthData.emit(stringBuilder.toString())

            stringBuilder.appendConsoleLine("Syncing Sleep summary of yesterday: $yesterday...")
            _syncHealthData.emit(stringBuilder.toString())

            syncSleepSummary(yesterday, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing Physical summary of yesterday: $yesterday...")
            _syncHealthData.emit(stringBuilder.toString())

            syncPhysicalSummary(yesterday, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing Body summary of yesterday: $yesterday...")
            _syncHealthData.emit(stringBuilder.toString())

            syncBodySummary(yesterday, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing Physical events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncPhysicalEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing BloodGlucose events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncBloodGlucoseEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing BloodPressure events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncBloodPressureEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing BodyMetrics events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncBodyMetricsEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing BodyHeartRate events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncBodyHeartRateEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing PhysicalHeartRate events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncPhysicalHeartRateEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing Hydration events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncHydrationEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing Nutrition events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncNutritionEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing BodyOxygenation events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncBodyOxygenationEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing PhysicalOxygenation events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncPhysicalOxygenationEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing Temperature events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncTemperatureEvents(today, stringBuilder)

            stringBuilder.appendConsoleLine("Syncing Steps events of today: $today...")
            _syncHealthData.emit(stringBuilder.toString())

            syncStepsEvents(stringBuilder)
        }
    }


    private suspend fun syncSleepSummary(yesterday: LocalDate, stringBuilder: StringBuilder) {
        RookHelpers.shouldSyncFor(HealthDataType.SLEEP_SUMMARY, yesterday).fold(
            { shouldSyncSummariesForYesterday ->
                if (shouldSyncSummariesForYesterday) {
                    rookSummaryManager.syncSleepSummary(yesterday).fold(
                        {
                            when (it) {
                                SyncStatus.RECORDS_NOT_FOUND -> {
                                    stringBuilder.appendConsoleLine("Sleep summary not found")
                                }

                                SyncStatus.SYNCED -> {
                                    stringBuilder.appendConsoleLine("Sleep summary synced successfully")
                                }
                            }

                            _syncHealthData.emit(stringBuilder.toString())
                        },
                        {
                            val error = when (it) {
                                is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                                is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                                is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                                is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                                is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                                is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                                is TimeoutException -> "TimeoutException: ${it.message}"
                                is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                                is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                                else -> "${it.message}"
                            }

                            stringBuilder.appendConsoleLine("Error syncing Sleep summary:")
                            stringBuilder.appendConsoleLine(error)
                            _syncHealthData.emit(stringBuilder.toString())
                        }
                    )
                } else {
                    stringBuilder.appendConsoleLine("Sleep summary was already synced for this day")
                    _syncHealthData.emit(stringBuilder.toString())
                }
            }, {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Sleep summary:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncPhysicalSummary(
        yesterday: LocalDate,
        stringBuilder: StringBuilder,
    ) {
        RookHelpers.shouldSyncFor(HealthDataType.PHYSICAL_SUMMARY, yesterday).fold(
            { shouldSyncSummariesForYesterday ->
                if (shouldSyncSummariesForYesterday) {
                    rookSummaryManager.syncPhysicalSummary(yesterday).fold(
                        {
                            when (it) {
                                SyncStatus.RECORDS_NOT_FOUND -> {
                                    stringBuilder.appendConsoleLine("Physical summary not found")
                                }

                                SyncStatus.SYNCED -> {
                                    stringBuilder.appendConsoleLine("Physical summary synced successfully")
                                }
                            }

                            _syncHealthData.emit(stringBuilder.toString())
                        },
                        {
                            val error = when (it) {
                                is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                                is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                                is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                                is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                                is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                                is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                                is TimeoutException -> "TimeoutException: ${it.message}"
                                is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                                is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                                else -> "${it.message}"
                            }

                            stringBuilder.appendConsoleLine("Error syncing Physical summary:")
                            stringBuilder.appendConsoleLine(error)
                            _syncHealthData.emit(stringBuilder.toString())
                        }
                    )
                } else {
                    stringBuilder.appendConsoleLine("Physical summary was already synced for this day")
                    _syncHealthData.emit(stringBuilder.toString())
                }
            }, {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Physical summary:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBodySummary(yesterday: LocalDate, stringBuilder: StringBuilder) {
        RookHelpers.shouldSyncFor(HealthDataType.BODY_SUMMARY, yesterday).fold(
            { shouldSyncSummariesForYesterday ->
                if (shouldSyncSummariesForYesterday) {
                    rookSummaryManager.syncBodySummary(yesterday).fold(
                        {
                            when (it) {
                                SyncStatus.RECORDS_NOT_FOUND -> {
                                    stringBuilder.appendConsoleLine("Body summary not found")
                                }

                                SyncStatus.SYNCED -> {
                                    stringBuilder.appendConsoleLine("Body summary synced successfully")
                                }
                            }

                            _syncHealthData.emit(stringBuilder.toString())
                        },
                        {
                            val error = when (it) {
                                is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                                is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                                is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                                is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                                is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                                is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                                is TimeoutException -> "TimeoutException: ${it.message}"
                                is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                                is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                                else -> "${it.message}"
                            }

                            stringBuilder.appendConsoleLine("Error syncing Body summary:")
                            stringBuilder.appendConsoleLine(error)
                            _syncHealthData.emit(stringBuilder.toString())
                        }
                    )
                } else {
                    stringBuilder.appendConsoleLine("Body summary was already synced for this day")
                    _syncHealthData.emit(stringBuilder.toString())
                }
            }, {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Body summary:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncPhysicalEvents(today: LocalDate, stringBuilder: StringBuilder) {
        rookEventManager.syncPhysicalEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("Physical events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("Physical events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Physical events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBloodGlucoseEvents(today: LocalDate, stringBuilder: StringBuilder) {
        rookEventManager.syncBloodGlucoseEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("BloodGlucose events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("BloodGlucose events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing BloodGlucose events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBloodPressureEvents(
        today: LocalDate,
        stringBuilder: StringBuilder,
    ) {
        rookEventManager.syncBloodPressureEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("BloodPressure events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("BloodPressure events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing BloodPressure events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBodyMetricsEvents(today: LocalDate, stringBuilder: StringBuilder) {
        rookEventManager.syncBodyMetricsEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("BodyMetrics events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("BodyMetrics events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing BodyMetrics events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBodyHeartRateEvents(
        today: LocalDate,
        stringBuilder: StringBuilder,
    ) {
        rookEventManager.syncBodyHeartRateEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("BodyHearRate events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("BodyHeartRate events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing BodyHeartRate events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncPhysicalHeartRateEvents(
        today: LocalDate,
        stringBuilder: StringBuilder,
    ) {
        rookEventManager.syncPhysicalHeartRateEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("PhysicalHeartRate events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("PhysicalHeartRate events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing PhysicalHeartRate events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncHydrationEvents(today: LocalDate, stringBuilder: StringBuilder) {
        rookEventManager.syncHydrationEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("Hydration events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("Hydration events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Hydration events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncNutritionEvents(today: LocalDate, stringBuilder: StringBuilder) {
        rookEventManager.syncNutritionEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("Nutrition events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("Nutrition events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Nutrition events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBodyOxygenationEvents(
        today: LocalDate,
        stringBuilder: StringBuilder,
    ) {
        rookEventManager.syncBodyOxygenationEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("BodyOxygenation events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("BodyOxygenation events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing BodyOxygenation events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncPhysicalOxygenationEvents(
        today: LocalDate,
        stringBuilder: StringBuilder,
    ) {
        rookEventManager.syncPhysicalOxygenationEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("PhysicalOxygenation events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("PhysicalOxygenation events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing PhysicalOxygenation events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncTemperatureEvents(today: LocalDate, stringBuilder: StringBuilder) {
        rookEventManager.syncTemperatureEvents(today).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> {
                        stringBuilder.appendConsoleLine("temperature events not found")
                    }

                    SyncStatus.SYNCED -> {
                        stringBuilder.appendConsoleLine("Temperature events synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Temperature events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncStepsEvents(stringBuilder: StringBuilder) {
        rookEventManager.syncTodayHealthConnectStepsCount().fold(
            {
                when (it) {
                    SyncStatusWithData.RecordsNotFound -> {
                        stringBuilder.appendConsoleLine("Steps events not found")
                    }

                    is SyncStatusWithData.Synced -> {
                        stringBuilder.appendConsoleLine("${it.data} steps synced successfully")
                    }
                }

                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is RequestQuotaExceededException -> "RequestQuotaExceededException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Steps events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    fun syncPendingSummaries() {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Syncing pending summaries...")
            _pendingSummaries.emit(stringBuilder.toString())

            val result = rookSummaryManager.syncPendingSummaries()

            result.fold(
                {
                    stringBuilder.appendConsoleLine("Pending summaries synced successfully")
                    _pendingSummaries.emit(stringBuilder.toString())
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                        is TimeoutException -> "TimeoutException: ${it.message}"
                        is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                        else -> "${it.message}"
                    }

                    stringBuilder.appendConsoleLine("Error syncing pending summaries:")
                    stringBuilder.appendConsoleLine(error)
                    _pendingSummaries.emit(stringBuilder.toString())
                }
            )
        }
    }

    fun syncPendingEvents() {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Syncing pending events...")
            _pendingEvents.emit(stringBuilder.toString())

            val result = rookEventManager.syncPendingEvents()

            result.fold(
                {
                    stringBuilder.appendConsoleLine("Pending events synced successfully")
                    _pendingEvents.emit(stringBuilder.toString())
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                        is TimeoutException -> "TimeoutException: ${it.message}"
                        is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                        else -> "${it.message}"
                    }

                    stringBuilder.appendConsoleLine("Error syncing pending events:")
                    stringBuilder.appendConsoleLine(error)
                    _pendingEvents.emit(stringBuilder.toString())
                }
            )
        }
    }
}