package com.rookmotion.rookconnectdemo.features.monolithic.sdkplayground

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.RookEventManager
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.RookSummaryManager
import com.rookmotion.rook.sdk.domain.enums.AvailabilityStatus
import com.rookmotion.rook.sdk.domain.enums.HealthPermission
import com.rookmotion.rook.sdk.domain.exception.DeviceNotSupportedException
import com.rookmotion.rook.sdk.domain.exception.HealthConnectNotInstalledException
import com.rookmotion.rook.sdk.domain.exception.HttpRequestException
import com.rookmotion.rook.sdk.domain.exception.MissingPermissionsException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.TimeoutException
import com.rookmotion.rook.sdk.domain.exception.UserNotInitializedException
import com.rookmotion.rookconnectdemo.extension.appendConsoleLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

class SDKPlaygroundViewModel(private val rookConfigurationManager: RookConfigurationManager) : ViewModel() {

    private val rookHealthPermissionsManager = RookHealthPermissionsManager(rookConfigurationManager)
    private val rookSummaryManager = RookSummaryManager(rookConfigurationManager)
    private val rookEventManager = RookEventManager(rookConfigurationManager)

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
                AvailabilityStatus.INSTALLED -> "Health Connect is installed! You can skip the next step"
                AvailabilityStatus.NOT_INSTALLED -> "Health Connect is not installed. Please download from the Play Store"
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

            val result = rookHealthPermissionsManager.checkPermissions(HealthPermission.ALL)

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

    fun registerPermissionsRequestLauncher(fragment: Fragment) {
        Timber.i("Registering all permissions request launcher...")
        RookHealthPermissionsManager.registerPermissionsRequestLauncher(fragment)
        Timber.i("All permissions request launcher registered")
    }

    fun unregisterPermissionsRequestLauncher() {
        Timber.i("Unregistering all permissions request launcher...")
        RookHealthPermissionsManager.unregisterPermissionsRequestLauncher()
        Timber.i("All permissions request launcher unregistered")
    }

    fun launchPermissionsRequest() {
        Timber.i("Launching all permissions request...")
        RookHealthPermissionsManager.launchPermissionsRequest(HealthPermission.ALL)
        Timber.i("All permissions request launch")
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
        }
    }


    private suspend fun syncSleepSummary(yesterday: LocalDate, stringBuilder: StringBuilder) {
        rookSummaryManager.shouldSyncSleepSummariesFor(yesterday).fold(
            { shouldSyncSummariesForYesterday ->
                if (shouldSyncSummariesForYesterday) {
                    val result = rookSummaryManager.syncSleepSummary(yesterday)

                    result.fold(
                        {
                            stringBuilder.appendConsoleLine("Sleep summary synced successfully")
                            _syncHealthData.emit(stringBuilder.toString())
                        },
                        {
                            val error = when (it) {
                                is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                                is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                                is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                                is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                                is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                                is TimeoutException -> "TimeoutException: ${it.message}"
                                is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
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
        rookSummaryManager.shouldSyncPhysicalSummariesFor(yesterday).fold(
            { shouldSyncSummariesForYesterday ->
                if (shouldSyncSummariesForYesterday) {
                    val result = rookSummaryManager.syncPhysicalSummary(yesterday)

                    result.fold(
                        {
                            stringBuilder.appendConsoleLine("Physical summary synced successfully")
                            _syncHealthData.emit(stringBuilder.toString())
                        },
                        {
                            val error = when (it) {
                                is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                                is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                                is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                                is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                                is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                                is TimeoutException -> "TimeoutException: ${it.message}"
                                is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Physical summary:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBodySummary(yesterday: LocalDate, stringBuilder: StringBuilder) {
        rookSummaryManager.shouldSyncBodySummariesFor(yesterday).fold(
            { shouldSyncSummariesForYesterday ->
                if (shouldSyncSummariesForYesterday) {
                    val result = rookSummaryManager.syncBodySummary(yesterday)

                    result.fold(
                        {
                            stringBuilder.appendConsoleLine("Body summary synced successfully")
                            _syncHealthData.emit(stringBuilder.toString())
                        },
                        {
                            val error = when (it) {
                                is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                                is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                                is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                                is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                                is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                                is TimeoutException -> "TimeoutException: ${it.message}"
                                is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Body summary:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncPhysicalEvents(today: LocalDate, stringBuilder: StringBuilder) {
        val result = rookEventManager.syncPhysicalEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("Physical events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Physical events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBloodGlucoseEvents(today: LocalDate, stringBuilder: StringBuilder) {
        val result = rookEventManager.syncBloodGlucoseEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("BloodGlucose events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
        val result = rookEventManager.syncBloodPressureEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("BloodPressure events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing BloodPressure events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncBodyMetricsEvents(today: LocalDate, stringBuilder: StringBuilder) {
        val result = rookEventManager.syncBodyMetricsEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("BodyMetrics events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
        val result = rookEventManager.syncBodyHeartRateEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("BodyHeartRate events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
        val result = rookEventManager.syncPhysicalHeartRateEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("PhysicalHeartRate events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing PhysicalHeartRate events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncHydrationEvents(today: LocalDate, stringBuilder: StringBuilder) {
        val result = rookEventManager.syncHydrationEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("Hydration events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Hydration events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncNutritionEvents(today: LocalDate, stringBuilder: StringBuilder) {
        val result = rookEventManager.syncNutritionEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("Nutrition events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
        val result = rookEventManager.syncBodyOxygenationEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("BodyOxygenation events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
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
        val result = rookEventManager.syncPhysicalOxygenationEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("PhysicalOxygenation events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing PhysicalOxygenation events:")
                stringBuilder.appendConsoleLine(error)
                _syncHealthData.emit(stringBuilder.toString())
            }
        )
    }

    private suspend fun syncTemperatureEvents(today: LocalDate, stringBuilder: StringBuilder) {
        val result = rookEventManager.syncTemperatureEvents(today)

        result.fold(
            {
                stringBuilder.appendConsoleLine("Temperature events synced successfully")
                _syncHealthData.emit(stringBuilder.toString())
            },
            {
                val error = when (it) {
                    is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                    is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                    is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                    is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                    is MissingPermissionsException -> "MissingPermissionsException: ${it.message}"
                    is TimeoutException -> "TimeoutException: ${it.message}"
                    is HttpRequestException -> "HttpRequestException: code: ${it.code} message: ${it.message}"
                    else -> "${it.message}"
                }

                stringBuilder.appendConsoleLine("Error syncing Temperature events:")
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

    fun syncYesterdayHealthData() {
        viewModelScope.launch {
            rookSummaryManager.syncYesterdaySummaries()
            rookEventManager.syncYesterdayEvents()
        }
    }
}