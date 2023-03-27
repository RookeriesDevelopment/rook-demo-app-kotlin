package com.rookmotion.rookconnectdemo.utils

import com.rookmotion.rook.health_connect.domain.model.*
import com.rookmotion.rook.transmission.domain.model.*

fun SleepSummary.toItem(): SleepSummaryItem {
    return SleepSummaryItem(
        sourceOfData = sourceOfData,
        dateTime = toUTCZonedDateTime(dateTime),
        sleepStartDatetime = toUTCZonedDateTime(sleepStartDatetime),
        sleepEndDatetime = toUTCZonedDateTime(sleepEndDatetime),
        sleepDate = toLocalDate(sleepDate),
        sleepDurationSeconds = sleepDurationSeconds,
        timeInBedSeconds = timeInBedSeconds,
        lightSleepDurationSeconds = lightSleepDurationSeconds,
        remSleepDurationSeconds = remSleepDurationSeconds,
        deepSleepDurationSeconds = deepSleepDurationSeconds,
        timeAwakeDuringSleepSeconds = timeAwakeDuringSleepSeconds,
    )
}

fun PhysicalSummary.toItem(): PhysicalSummaryItem {
    return PhysicalSummaryItem(
        sourceOfData = sourceOfData,
        dateTime = toUTCZonedDateTime(dateTime),
        stepsPerDay = stepsPerDay,
        stepsGranularDataStepsPerHr = stepsGranularDataStepsPerHr.map { it.toItem() },
        traveledDistanceMeters = traveledDistanceMeters,
        traveledDistanceGranularDataMeters = traveledDistanceGranularDataMeters.map { it.toItem() },
        floorsClimbed = floorsClimbed,
        floorsClimbedGranularData = floorsClimbedGranularData.map { it.toItem() },
        elevationAvgAltitudeMeters = elevationAvgAltitudeMeters,
        elevationMinimumAltitudeMeters = elevationMinimumAltitudeMeters,
        elevationMaxAltitudeMeters = elevationMaxAltitudeMeters,
        elevationGranularDataMeters = elevationGranularDataMeters.map { it.toItem() },
        saturationAvgPercentage = saturationAvgPercentage,
        saturationGranularDataPercentage = saturationGranularDataPercentage.map { it.toItem() },
        vo2MaxAvgMlPerMinPerKg = vo2MaxAvgMlPerMinPerKg,
        vo2GranularDataLiterPerMin = vo2MaxGranularDataMlPerMinPerKg.map { it.toItem() },
        caloriesExpenditureKilocalories = caloriesExpenditureKilocalories,
        caloriesNetActiveKilocalories = caloriesNetActiveKilocalories,
        hrMaxBpm = hrMaxBpm,
        hrMinimumBpm = hrMinimumBpm,
        hrAvgBpm = hrAvgBpm,
        hrAvgRestingBpm = hrAvgRestingBpm,
        hrGranularDataBpm = hrGranularDataBpm.map { it.toItem() },
        hrvAvgRmssd = hrvAvgRmssd,
        hrvRmssdGranularData = hrvRmssdGranularData.map { it.toItem() },
    )
}

fun PhysicalEvents.toItems(): List<PhysicalEventItem> {
    return events.map { it.toItem() }
}

fun PhysicalEvent.toItem(): PhysicalEventItem {
    return PhysicalEventItem(
        sourceOfData = sourceOfData,
        dateTime = toUTCZonedDateTime(dateTime),
        activityStartDatetime = toUTCZonedDateTime(activityStartDatetime),
        activityEndDatetime = toUTCZonedDateTime(activityEndDatetime),
        activityDurationSeconds = activityDurationSeconds,
        activityTypeName = activityTypeName,
        caloriesExpenditureKilocalories = caloriesExpenditureKilocalories,
        caloriesNetActiveKilocalories = caloriesNetActiveKilocalories,
        steps = steps,
        traveledDistanceMeters = traveledDistanceMeters,
        traveledDistanceGranularDataMeters = traveledDistanceGranularDataMeters.map { it.toItem() },
        floorsClimbed = floorsClimbed,
        floorsClimbedGranularData = floorsClimbedGranularData.map { it.toItem() },
        elevationAvgAltitudeMeters = elevationAvgAltitudeMeters,
        elevationMinimumAltitudeMeters = elevationMinimumAltitudeMeters,
        elevationMaxAltitudeMeters = elevationMaxAltitudeMeters,
        elevationGranularDataMeters = elevationGranularDataMeters.map { it.toItem() },
        hrMaxBpm = hrMaxBpm,
        hrMinimumBpm = hrMinimumBpm,
        hrAvgBpm = hrAvgBpm,
        hrAvgRestingBpm = hrAvgRestingBpm,
        hrGranularDataBpm = hrGranularDataBpm.map { it.toItem() },
        hrvAvgRmssd = hrvAvgRmssd,
        hrvRmssdGranularData = hrvRmssdGranularData.map { it.toItem() },
        speedAvgMetersPerSecond = speedAvgMetersPerSecond,
        speedMaxMetersPerSecond = speedMaxMetersPerSecond,
        speedGranularDataMetersPerSecond = speedGranularDataMetersPerSecond.map { it.toItem() },
        cadenceAvgRpm = cadenceAvgRpm,
        cadenceMaxRpm = cadenceMaxRpm,
        cadenceGranularDataRpm = cadenceGranularDataRpm.map { it.toItem() },
        saturationAvgPercentage = saturationAvgPercentage,
        saturationGranularDataPercentage = saturationGranularDataPercentage.map { it.toItem() },
        vo2MaxAvgMlPerMinPerKg = vo2MaxAvgMlPerMinPerKg,
        vo2GranularDataLiterPerMin = vo2MaxGranularDataMlPerMinPerKg.map { it.toItem() },
    )
}

fun BodySummary.toItem(): BodySummaryItem {
    return BodySummaryItem(
        sourceOfData = sourceOfData,
        dateTime = toUTCZonedDateTime(dateTime),
        weightKg = weightKg,
        heightCm = heightCm,
        bloodGlucoseDayAvgMgPerDl = bloodGlucoseDayAvgMgPerDl,
        bloodGlucoseGranularDataMgPerDL = bloodGlucoseGranularDataMgPerDl.map { it.toItem() },
        bloodPressureDayAvgSystolicDiastolic = bloodPressureDayAvgSystolicDiastolic?.toItem(),
        bloodPressureGranularDataSystolicDiastolic = bloodPressureGranularDataSystolicDiastolic.map { it.toItem() },
        hydrationAmountGranularDataMl = hydrationAmountGranularDataMl.map { it.toItem() },
        hrMaxBpm = hrMaxBpm,
        hrMinimumBpm = hrMinimumBpm,
        hrAvgBpm = hrAvgBpm,
        hrAvgRestingBpm = hrAvgRestingBpm,
        hrGranularDataBpm = hrGranularDataBpm.map { it.toItem() },
        hrvAvgRmssd = hrvAvgRmssd,
        hrvRmssdGranularData = hrvRmssdGranularData.map { it.toItem() },
        saturationAvgPercentage = saturationAvgPercentage,
        saturationGranularDataPercentage = saturationGranularDataPercentage.map { it.toItem() },
        vo2MaxAvgMlPerMinPerKg = vo2MaxAvgMlPerMinPerKg,
        vo2GranularDataLiterPerMin = vo2MaxGranularDataMlPerMinPerKg.map { it.toItem() },
        temperatureMinimumCelsius = temperatureMinimumCelsius,
        temperatureAvgCelsius = temperatureAvgCelsius,
        temperatureMaxCelsius = temperatureMaxCelsius,
        temperatureGranularDataCelsius = temperatureGranularDataCelsius.map { it.toItem() },
    )
}

fun StepsGranularDataStepsPerHr.toItem(): StepsGranularDataStepsPerHrItem {
    return StepsGranularDataStepsPerHrItem(
        dateTime = toUTCZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        steps = steps,
    )
}

fun TraveledDistanceGranularDataMeters.toItem(): TraveledDistanceGranularDataMetersItem {
    return TraveledDistanceGranularDataMetersItem(
        dateTime = toUTCZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        traveledDistanceMeters = traveledDistanceMeters,
    )
}

fun FloorsClimbedGranularData.toItem(): FloorsClimbedGranularDataItem {
    return FloorsClimbedGranularDataItem(
        dateTime = toUTCZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        floorsClimbed = floorsClimbed,
    )
}

fun ElevationGranularDataMeters.toItem(): ElevationGranularDataMetersItem {
    return ElevationGranularDataMetersItem(
        dateTime = toUTCZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        elevationChange = elevationChange,
    )
}

fun SaturationGranularDataPercentage.toItem(): SaturationGranularDataPercentageItem {
    return SaturationGranularDataPercentageItem(
        dateTime = toUTCZonedDateTime(dateTime),
        saturationPercentage = saturationPercentage,
    )
}

fun Vo2MaxGranularDataMlPerMinPerKg.toItem(): Vo2GranularDataLiterPerMinItem {
    return Vo2GranularDataLiterPerMinItem(
        dateTime = toUTCZonedDateTime(dateTime),
        vo2MlPerMinPerKg = vo2MaxMlPerMinPerKg,
    )
}

fun HrGranularDataBpm.toItem(): HrGranularDataBpmItem {
    return HrGranularDataBpmItem(
        dateTime = toUTCZonedDateTime(dateTime),
        hrBpm = hrBpm,
    )
}

fun HrvRmssdGranularData.toItem(): HrvRmssdGranularDataItem {
    return HrvRmssdGranularDataItem(
        dateTime = toUTCZonedDateTime(dateTime),
        hrvRmssd = hrvRmssd,
    )
}

fun SpeedGranularDataMetersPerSecond.toItem(): SpeedGranularDataMetersPerSecondItem {
    return SpeedGranularDataMetersPerSecondItem(
        dateTime = toUTCZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        speedMetersPerSecond = speedMetersPerSecond,
    )
}

fun CadenceGranularDataRpm.toItem(): CadenceGranularDataRpmItem {
    return CadenceGranularDataRpmItem(
        dateTime = toUTCZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        cadenceRpm = cadenceRpm,
    )
}

fun BloodGlucoseGranularDataMgPerDL.toItem(): BloodGlucoseGranularDataMgPerDLItem {
    return BloodGlucoseGranularDataMgPerDLItem(
        dateTime = toUTCZonedDateTime(dateTime),
        bloodGlucoseMgPerDL = bloodGlucoseMgPerDl,
    )
}

fun BloodPressureSystolicDiastolic.toItem(): BloodPressureSystolicDiastolicItem {
    return BloodPressureSystolicDiastolicItem(
        systolicBp = systolicBp,
        diastolicBp = diastolicBp
    )
}

fun BloodPressureGranularDataSystolicDiastolic.toItem(): BloodPressureGranularDataSystolicDiastolicItem {
    return BloodPressureGranularDataSystolicDiastolicItem(
        dateTime = toUTCZonedDateTime(dateTime),
        systolicBp = bloodPressureSystolicDiastolic.systolicBp,
        diastolicBp = bloodPressureSystolicDiastolic.diastolicBp
    )
}

fun HydrationAmountGranularDataMl.toItem(): HydrationAmountGranularDataMlItem {
    return HydrationAmountGranularDataMlItem(
        dateTime = toUTCZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        hydrationAmountMl = hydrationAmountMl,
    )
}

fun TemperatureGranularDataCelsius.toItem(): TemperatureGranularDataCelsiusItem {
    return TemperatureGranularDataCelsiusItem(
        dateTime = toUTCZonedDateTime(dateTime),
        temperatureCelsius = temperatureCelsius,
    )
}