package com.rookmotion.rookconnectdemo.utils

import com.rookmotion.rook.health_connect.domain.model.*
import com.rookmotion.rook.transmission.domain.model.*
import com.rookmotion.rookconnectdemo.utils.RookDateTimeUtils.stringToLocalDate
import com.rookmotion.rookconnectdemo.utils.RookDateTimeUtils.stringToZonedDateTime

fun SleepSummary.toItem(): SleepSummaryItem {
    return SleepSummaryItem(
        sourceOfData = sourceOfData,
        dateTime = stringToZonedDateTime(dateTime),
        sleepStartDatetime = stringToZonedDateTime(sleepStartDatetime),
        sleepEndDatetime = stringToZonedDateTime(sleepEndDatetime),
        sleepDate = stringToLocalDate(sleepDate),
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
        dateTime = stringToZonedDateTime(dateTime),
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
        dateTime = stringToZonedDateTime(dateTime),
        activityStartDatetime = stringToZonedDateTime(activityStartDatetime),
        activityEndDatetime = stringToZonedDateTime(activityEndDatetime),
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
        dateTime = stringToZonedDateTime(dateTime),
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
        dateTime = stringToZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        steps = steps,
    )
}

fun TraveledDistanceGranularDataMeters.toItem(): TraveledDistanceGranularDataMetersItem {
    return TraveledDistanceGranularDataMetersItem(
        dateTime = stringToZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        traveledDistanceMeters = traveledDistanceMeters,
    )
}

fun FloorsClimbedGranularData.toItem(): FloorsClimbedGranularDataItem {
    return FloorsClimbedGranularDataItem(
        dateTime = stringToZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        floorsClimbed = floorsClimbed,
    )
}

fun ElevationGranularDataMeters.toItem(): ElevationGranularDataMetersItem {
    return ElevationGranularDataMetersItem(
        dateTime = stringToZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        elevationChange = elevationChange,
    )
}

fun SaturationGranularDataPercentage.toItem(): SaturationGranularDataPercentageItem {
    return SaturationGranularDataPercentageItem(
        dateTime = stringToZonedDateTime(dateTime),
        saturationPercentage = saturationPercentage,
    )
}

fun Vo2MaxGranularDataMlPerMinPerKg.toItem(): Vo2GranularDataLiterPerMinItem {
    return Vo2GranularDataLiterPerMinItem(
        dateTime = stringToZonedDateTime(dateTime),
        vo2MlPerMinPerKg = vo2MaxMlPerMinPerKg,
    )
}

fun HrGranularDataBpm.toItem(): HrGranularDataBpmItem {
    return HrGranularDataBpmItem(
        dateTime = stringToZonedDateTime(dateTime),
        hrBpm = hrBpm,
    )
}

fun HrvRmssdGranularData.toItem(): HrvRmssdGranularDataItem {
    return HrvRmssdGranularDataItem(
        dateTime = stringToZonedDateTime(dateTime),
        hrvRmssd = hrvRmssd,
    )
}

fun SpeedGranularDataMetersPerSecond.toItem(): SpeedGranularDataMetersPerSecondItem {
    return SpeedGranularDataMetersPerSecondItem(
        dateTime = stringToZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        speedMetersPerSecond = speedMetersPerSecond,
    )
}

fun CadenceGranularDataRpm.toItem(): CadenceGranularDataRpmItem {
    return CadenceGranularDataRpmItem(
        dateTime = stringToZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        cadenceRpm = cadenceRpm,
    )
}

fun BloodGlucoseGranularDataMgPerDL.toItem(): BloodGlucoseGranularDataMgPerDLItem {
    return BloodGlucoseGranularDataMgPerDLItem(
        dateTime = stringToZonedDateTime(dateTime),
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
        dateTime = stringToZonedDateTime(dateTime),
        systolicBp = bloodPressureSystolicDiastolic.systolicBp,
        diastolicBp = bloodPressureSystolicDiastolic.diastolicBp
    )
}

fun HydrationAmountGranularDataMl.toItem(): HydrationAmountGranularDataMlItem {
    return HydrationAmountGranularDataMlItem(
        dateTime = stringToZonedDateTime(dateTime),
        intervalDurationSeconds = 0,
        hydrationAmountMl = hydrationAmountMl,
    )
}

fun TemperatureGranularDataCelsius.toItem(): TemperatureGranularDataCelsiusItem {
    return TemperatureGranularDataCelsiusItem(
        dateTime = stringToZonedDateTime(dateTime),
        temperatureCelsius = temperatureCelsius,
    )
}