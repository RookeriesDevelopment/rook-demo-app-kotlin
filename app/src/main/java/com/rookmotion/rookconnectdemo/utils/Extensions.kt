package com.rookmotion.rookconnectdemo.utils

import com.rookmotion.rook.health_connect.domain.model.*
import com.rookmotion.rook.transmission.domain.model.BodySummaryItem
import com.rookmotion.rook.transmission.domain.model.PhysicalEventItem
import com.rookmotion.rook.transmission.domain.model.PhysicalSummaryItem
import com.rookmotion.rook.transmission.domain.model.SleepSummaryItem
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.toRookDateTimeString(): String {
    return "${DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(this)}.000Z"
}

fun SleepSummary.toItem(): SleepSummaryItem {
    return SleepSummaryItem(
        sourceOfData = "Health Connect",
        dateTime = dateTime,
        sleepStartDatetime = sleepStartDatetime,
        sleepEndDatetime = sleepEndDatetime,
        sleepDate = sleepDate,
        sleepDurationSeconds = sleepDurationSeconds,
        timeInBedSeconds = timeInBedSeconds,
        lightSleepDurationSeconds = lightSleepDurationSeconds,
    )
}

fun PhysicalSummary.toItem(): PhysicalSummaryItem {
    return PhysicalSummaryItem(
        sourceOfData = "Health Connect",
        dateTime = dateTime,
        stepsPerDay = stepsPerDay,
        traveledDistanceMeters = traveledDistanceMeters,
        floorsClimbed = floorsClimbed,
        elevationAvgAltitudeMeters = elevationAvgAltitudeMeters,
        elevationMinimumAltitudeMeters = elevationMinimumAltitudeMeters,
        elevationMaxAltitudeMeters = elevationMaxAltitudeMeters,
        saturationAvgPercentage = saturationAvgPercentage,
        vo2MaxAvgMlPerMinPerKg = vo2MaxAvgMlPerMinPerKg,
        caloriesExpenditureKilocalories = caloriesExpenditureKilocalories,
        caloriesNetActiveKilocalories = caloriesNetActiveKilocalories,
        hrMaxBpm = hrMaxBpm,
        hrMinimumBpm = hrMinimumBpm,
        hrAvgBpm = hrAvgBpm,
        hrAvgRestingBpm = hrAvgRestingBpm,
        hrvAvgRmssd = hrvAvgRmssd,
    )
}

fun PhysicalEvents.toItems(): List<PhysicalEventItem> {
    return events.map { it.toItem() }
}

fun PhysicalEvent.toItem(): PhysicalEventItem {
    return PhysicalEventItem(
        sourceOfData = "Health Connect",
        dateTime = dateTime,
        activityStartDatetime = activityStartDatetime,
        activityEndDatetime = activityEndDatetime,
        activityDurationSeconds = activityDurationSeconds,
        activityTypeName = activityTypeName,
        caloriesExpenditureKilocalories = caloriesExpenditureKilocalories,
        caloriesNetActiveKilocalories = caloriesNetActiveKilocalories,
        steps = steps,
        traveledDistanceMeters = traveledDistanceMeters,
        floorsClimbed = floorsClimbed,
        elevationAvgAltitudeMeters = elevationAvgAltitudeMeters,
        elevationMinimumAltitudeMeters = elevationMinimumAltitudeMeters,
        elevationMaxAltitudeMeters = elevationMaxAltitudeMeters,
        hrMaxBpm = hrMaxBpm,
        hrMinimumBpm = hrMinimumBpm,
        hrAvgBpm = hrAvgBpm,
        hrAvgRestingBpm = hrAvgRestingBpm,
        hrvAvgRmssd = hrvAvgRmssd,
        speedAvgMetersPerSecond = speedAvgMetersPerSecond,
        speedMaxMetersPerSecond = speedMaxMetersPerSecond,
        cadenceAvgRpm = cadenceAvgRpm,
        cadenceMaxRpm = cadenceMaxRpm,
        saturationAvgPercentage = saturationAvgPercentage,
        vo2MaxAvgMlPerMinPerKg = vo2MaxAvgMlPerMinPerKg,
    )
}

fun BodySummary.toItem(): BodySummaryItem {
    return BodySummaryItem(
        sourceOfData = "Health Connect",
        dateTime = dateTime,
        weightKg = weightKg,
        heightCm = heightCm,
        bloodGlucoseDayAvgMgPerDl = bloodGlucoseDayAvgMgPerDl,
        hrMaxBpm = hrMaxBpm,
        hrMinimumBpm = hrMinimumBpm,
        hrAvgBpm = hrAvgBpm,
        hrAvgRestingBpm = hrAvgRestingBpm,
        hrvAvgRmssd = hrvAvgRmssd,
        saturationAvgPercentage = saturationAvgPercentage,
        vo2MaxAvgMlPerMinPerKg = vo2MaxAvgMlPerMinPerKg,
        temperatureMinimumCelsius = temperatureMinimumCelsius,
        temperatureAvgCelsius = temperatureAvgCelsius,
        temperatureMaxCelsius = temperatureMaxCelsius,
    )
}