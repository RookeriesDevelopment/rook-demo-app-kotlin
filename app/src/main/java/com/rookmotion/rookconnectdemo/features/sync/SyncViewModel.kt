package com.rookmotion.rookconnectdemo.features.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookEventManager
import com.rookmotion.rook.sdk.RookSummaryManager
import com.rookmotion.rook.sdk.domain.enums.SyncStatus
import com.rookmotion.rook.sdk.domain.model.SyncStatusWithData
import com.rookmotion.rookconnectdemo.common.ConsoleOutput
import kotlinx.coroutines.launch
import java.time.LocalDate

class SyncViewModel(
    private val rookSummaryManager: RookSummaryManager,
    private val rookEventManager: RookEventManager,
) : ViewModel() {

    val syncSummariesOutput = ConsoleOutput()
    val syncEventsOutput = ConsoleOutput()
    val syncPendingSummariesOutput = ConsoleOutput()
    val syncPendingEventsOutput = ConsoleOutput()

    fun syncSummaries(localDate: LocalDate) {
        syncSummariesOutput.set("Syncing summaries of $localDate...")

        viewModelScope.launch {
            syncSleepSummary(localDate)
            syncPhysicalSummary(localDate)
            syncBodySummary(localDate)
        }
    }

    private suspend fun syncSleepSummary(localDate: LocalDate) {
        syncSummariesOutput.append("Syncing Sleep summary of $localDate...")

        rookSummaryManager.syncSleepSummary(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncSummariesOutput.append("Sleep summary not found")
                    SyncStatus.SYNCED -> syncSummariesOutput.append("Sleep summary synced successfully")
                }
            },
            {
                syncSummariesOutput.appendError(it, "Error syncing Sleep summary")
            }
        )
    }

    private suspend fun syncPhysicalSummary(localDate: LocalDate) {
        syncSummariesOutput.append("Syncing Physical summary of $localDate...")

        rookSummaryManager.syncPhysicalSummary(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncSummariesOutput.append("Physical summary not found")
                    SyncStatus.SYNCED -> syncSummariesOutput.append("Physical summary synced successfully")
                }
            },
            {
                syncSummariesOutput.appendError(it, "Error syncing Physical summary")
            }
        )
    }

    private suspend fun syncBodySummary(localDate: LocalDate) {
        syncSummariesOutput.append("Syncing Body summary of $localDate...")

        rookSummaryManager.syncBodySummary(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncSummariesOutput.append("Body summary not found")
                    SyncStatus.SYNCED -> syncSummariesOutput.append("Body summary synced successfully")
                }
            },
            {
                syncSummariesOutput.appendError(it, "Error syncing Body summary")
            }
        )
    }

    fun syncEvents(localDate: LocalDate) {
        syncEventsOutput.set("Syncing events of $localDate...")

        viewModelScope.launch {
            syncPhysicalEvents(localDate)
            syncBloodGlucoseEvents(localDate)
            syncBloodPressureEvents(localDate)
            syncBodyMetricsEvents(localDate)
            syncBodyHeartRateEvents(localDate)
            syncPhysicalHeartRateEvents(localDate)
            syncHydrationEvents(localDate)
            syncNutritionEvents(localDate)
            syncBodyOxygenationEvents(localDate)
            syncPhysicalOxygenationEvents(localDate)
            syncTemperatureEvents(localDate)
            syncStepsEvents()
            syncCaloriesEvents()
        }
    }

    private suspend fun syncPhysicalEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing Physical events of $localDate...")

        rookEventManager.syncPhysicalEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("Physical events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("Physical events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing Physical events")
            }
        )
    }

    private suspend fun syncBloodGlucoseEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing BloodGlucose events of $localDate...")

        rookEventManager.syncBloodGlucoseEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("BloodGlucose events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("BloodGlucose events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing BloodGlucose events")
            }
        )
    }

    private suspend fun syncBloodPressureEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing BloodPressure events of $localDate...")

        rookEventManager.syncBloodPressureEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("BloodPressure events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("BloodPressure events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing BloodPressure events")
            }
        )
    }

    private suspend fun syncBodyMetricsEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing BodyMetrics events of $localDate...")

        rookEventManager.syncBodyMetricsEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("BodyMetrics events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("BodyMetrics events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing BodyMetrics events")
            }
        )
    }

    private suspend fun syncBodyHeartRateEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing BodyHeartRate events of $localDate...")

        rookEventManager.syncBodyHeartRateEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("BodyHeartRate events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("BodyHeartRate events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing BodyHeartRate events")
            }
        )
    }

    private suspend fun syncPhysicalHeartRateEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing PhysicalHeartRate events of $localDate...")

        rookEventManager.syncPhysicalHeartRateEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("PhysicalHeartRate events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("PhysicalHeartRate events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing PhysicalHeartRate events")
            }
        )
    }

    private suspend fun syncHydrationEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing Hydration events of $localDate...")

        rookEventManager.syncHydrationEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("Hydration events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("Hydration events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing Hydration events")
            }
        )
    }

    private suspend fun syncNutritionEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing Nutrition events of $localDate...")

        rookEventManager.syncNutritionEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("Nutrition events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("Nutrition events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing Nutrition events")
            }
        )
    }

    private suspend fun syncBodyOxygenationEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing BodyOxygenation events of $localDate...")

        rookEventManager.syncBodyOxygenationEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("BodyOxygenation events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("BodyOxygenation events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing BodyOxygenation events")
            }
        )
    }

    private suspend fun syncPhysicalOxygenationEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing PhysicalOxygenation events of $localDate...")

        rookEventManager.syncPhysicalOxygenationEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("PhysicalOxygenation events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("PhysicalOxygenation events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing PhysicalOxygenation events")
            }
        )
    }

    private suspend fun syncTemperatureEvents(localDate: LocalDate) {
        syncEventsOutput.append("Syncing Temperature events of $localDate...")

        rookEventManager.syncTemperatureEvents(localDate).fold(
            {
                when (it) {
                    SyncStatus.RECORDS_NOT_FOUND -> syncEventsOutput.append("Temperature events not found")
                    SyncStatus.SYNCED -> syncEventsOutput.append("Temperature events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing Temperature events")
            }
        )
    }

    private suspend fun syncStepsEvents() {
        syncEventsOutput.append("Syncing Steps events of today...")

        rookEventManager.syncTodayHealthConnectStepsCount().fold(
            {
                when (it) {
                    SyncStatusWithData.RecordsNotFound -> syncEventsOutput.append("Steps events not found")
                    is SyncStatusWithData.Synced -> syncEventsOutput.append("Steps events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing Steps events")
            }
        )
    }

    private suspend fun syncCaloriesEvents() {
        syncEventsOutput.append("Syncing Calories events of today...")

        rookEventManager.getTodayCaloriesCount().fold(
            {
                when (it) {
                    SyncStatusWithData.RecordsNotFound -> syncEventsOutput.append("Calories events not found")
                    is SyncStatusWithData.Synced -> syncEventsOutput.append("Calories events synced successfully")
                }
            },
            {
                syncEventsOutput.appendError(it, "Error syncing Calories events")
            }
        )
    }

    fun syncPendingSummaries() {
        syncPendingSummariesOutput.set("Syncing pending summaries...")

        viewModelScope.launch {
            rookSummaryManager.syncPendingSummaries().fold(
                {
                    syncPendingSummariesOutput.append("Pending summaries synced successfully")
                },
                {
                    syncPendingSummariesOutput.appendError(it, "Error syncing pending summaries")
                }
            )
        }
    }

    fun syncPendingEvents() {
        syncPendingEventsOutput.set("Syncing pending events...")

        viewModelScope.launch {
            rookEventManager.syncPendingEvents().fold(
                {
                    syncPendingEventsOutput.append("Pending events synced successfully")
                },
                {
                    syncPendingEventsOutput.appendError(it, "Error syncing pending events")
                }
            )
        }
    }
}