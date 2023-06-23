package com.rookmotion.rookconnectdemo.ui.health_connect.playground

import com.rookmotion.rook.health_connect.domain.model.HCBloodGlucoseGranularDataMgPerDL
import com.rookmotion.rook.health_connect.domain.model.HCBloodPressureGranularDataSystolicDiastolic
import com.rookmotion.rook.health_connect.domain.model.HCBloodPressureSystolicDiastolic
import com.rookmotion.rook.health_connect.domain.model.HCBodySummary
import com.rookmotion.rook.health_connect.domain.model.HCCadenceGranularDataRpm
import com.rookmotion.rook.health_connect.domain.model.HCElevationGranularDataMeters
import com.rookmotion.rook.health_connect.domain.model.HCFloorsClimbedGranularData
import com.rookmotion.rook.health_connect.domain.model.HCHrGranularDataBpm
import com.rookmotion.rook.health_connect.domain.model.HCHrvRmssdGranularData
import com.rookmotion.rook.health_connect.domain.model.HCHydrationAmountGranularDataMl
import com.rookmotion.rook.health_connect.domain.model.HCPhysicalEvent
import com.rookmotion.rook.health_connect.domain.model.HCPhysicalEvents
import com.rookmotion.rook.health_connect.domain.model.HCPhysicalSummary
import com.rookmotion.rook.health_connect.domain.model.HCSaturationGranularDataPercentage
import com.rookmotion.rook.health_connect.domain.model.HCSleepSummary
import com.rookmotion.rook.health_connect.domain.model.HCSpeedGranularDataMetersPerSecond
import com.rookmotion.rook.health_connect.domain.model.HCStepsGranularDataStepsPerHr
import com.rookmotion.rook.health_connect.domain.model.HCTemperatureGranularDataCelsius
import com.rookmotion.rook.health_connect.domain.model.HCTraveledDistanceGranularDataMeters
import com.rookmotion.rook.health_connect.domain.model.HCVo2MaxGranularDataMlPerMinPerKg
import com.rookmotion.rook.transmission.domain.model.BloodGlucoseGranularDataMgPerDLItem
import com.rookmotion.rook.transmission.domain.model.BloodPressureGranularDataSystolicDiastolicItem
import com.rookmotion.rook.transmission.domain.model.BloodPressureSystolicDiastolicItem
import com.rookmotion.rook.transmission.domain.model.BodySummaryItem
import com.rookmotion.rook.transmission.domain.model.CadenceGranularDataRpmItem
import com.rookmotion.rook.transmission.domain.model.ElevationGranularDataMetersItem
import com.rookmotion.rook.transmission.domain.model.FloorsClimbedGranularDataItem
import com.rookmotion.rook.transmission.domain.model.HrGranularDataBpmItem
import com.rookmotion.rook.transmission.domain.model.HrvRmssdGranularDataItem
import com.rookmotion.rook.transmission.domain.model.HydrationAmountGranularDataMlItem
import com.rookmotion.rook.transmission.domain.model.PhysicalEventItem
import com.rookmotion.rook.transmission.domain.model.PhysicalSummaryItem
import com.rookmotion.rook.transmission.domain.model.SaturationGranularDataPercentageItem
import com.rookmotion.rook.transmission.domain.model.SleepSummaryItem
import com.rookmotion.rook.transmission.domain.model.SpeedGranularDataMetersPerSecondItem
import com.rookmotion.rook.transmission.domain.model.StepsGranularDataStepsPerHrItem
import com.rookmotion.rook.transmission.domain.model.TemperatureGranularDataCelsiusItem
import com.rookmotion.rook.transmission.domain.model.TraveledDistanceGranularDataMetersItem
import com.rookmotion.rook.transmission.domain.model.Vo2GranularDataLiterPerMinItem

fun HCSleepSummary.toItem(): SleepSummaryItem {
    return SleepSummaryItem(
        sourceOfData = sourceOfData,
        dateTime = dateTime,
        sleepStartDatetime = sleepStartDatetime,
        sleepEndDatetime = sleepEndDatetime,
        sleepDate = sleepDate,
        sleepDurationSeconds = sleepDurationSeconds,
        timeInBedSeconds = timeInBedSeconds,
        lightSleepDurationSeconds = lightSleepDurationSeconds,
        remSleepDurationSeconds = remSleepDurationSeconds,
        deepSleepDurationSeconds = deepSleepDurationSeconds,
        timeAwakeDuringSleepSeconds = timeAwakeDuringSleepSeconds,
    )
}

fun HCPhysicalSummary.toItem(): PhysicalSummaryItem {
    return PhysicalSummaryItem(
        sourceOfData = sourceOfData,
        dateTime = dateTime,
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

fun HCPhysicalEvents.toItems(): List<PhysicalEventItem> {
    return events.map { it.toItem() }
}

fun HCPhysicalEvent.toItem(): PhysicalEventItem {
    return PhysicalEventItem(
        sourceOfData = sourceOfData,
        dateTime = dateTime,
        activityStartDatetime = activityStartDatetime,
        activityEndDatetime = activityEndDatetime,
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

fun HCBodySummary.toItem(): BodySummaryItem {
    return BodySummaryItem(
        sourceOfData = sourceOfData,
        dateTime = dateTime,
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

fun HCStepsGranularDataStepsPerHr.toItem(): StepsGranularDataStepsPerHrItem {
    return StepsGranularDataStepsPerHrItem(
        dateTime = dateTime,
        intervalDurationSeconds = 0,
        steps = steps,
    )
}

fun HCTraveledDistanceGranularDataMeters.toItem(): TraveledDistanceGranularDataMetersItem {
    return TraveledDistanceGranularDataMetersItem(
        dateTime = dateTime,
        intervalDurationSeconds = 0,
        traveledDistanceMeters = traveledDistanceMeters,
    )
}

fun HCFloorsClimbedGranularData.toItem(): FloorsClimbedGranularDataItem {
    return FloorsClimbedGranularDataItem(
        dateTime = dateTime,
        intervalDurationSeconds = 0,
        floorsClimbed = floorsClimbed,
    )
}

fun HCElevationGranularDataMeters.toItem(): ElevationGranularDataMetersItem {
    return ElevationGranularDataMetersItem(
        dateTime = dateTime,
        intervalDurationSeconds = 0,
        elevationChange = elevationChange,
    )
}

fun HCSaturationGranularDataPercentage.toItem(): SaturationGranularDataPercentageItem {
    return SaturationGranularDataPercentageItem(
        dateTime = dateTime,
        saturationPercentage = saturationPercentage,
    )
}

fun HCVo2MaxGranularDataMlPerMinPerKg.toItem(): Vo2GranularDataLiterPerMinItem {
    return Vo2GranularDataLiterPerMinItem(
        dateTime = dateTime,
        vo2MlPerMinPerKg = vo2MaxMlPerMinPerKg,
    )
}

fun HCHrGranularDataBpm.toItem(): HrGranularDataBpmItem {
    return HrGranularDataBpmItem(
        dateTime = dateTime,
        hrBpm = hrBpm,
    )
}

fun HCHrvRmssdGranularData.toItem(): HrvRmssdGranularDataItem {
    return HrvRmssdGranularDataItem(
        dateTime = dateTime,
        hrvRmssd = hrvRmssd,
    )
}

fun HCSpeedGranularDataMetersPerSecond.toItem(): SpeedGranularDataMetersPerSecondItem {
    return SpeedGranularDataMetersPerSecondItem(
        dateTime = dateTime,
        intervalDurationSeconds = 0,
        speedMetersPerSecond = speedMetersPerSecond,
    )
}

fun HCCadenceGranularDataRpm.toItem(): CadenceGranularDataRpmItem {
    return CadenceGranularDataRpmItem(
        dateTime = dateTime,
        intervalDurationSeconds = 0,
        cadenceRpm = cadenceRpm,
    )
}

fun HCBloodGlucoseGranularDataMgPerDL.toItem(): BloodGlucoseGranularDataMgPerDLItem {
    return BloodGlucoseGranularDataMgPerDLItem(
        dateTime = dateTime,
        bloodGlucoseMgPerDL = bloodGlucoseMgPerDl,
    )
}

fun HCBloodPressureSystolicDiastolic.toItem(): BloodPressureSystolicDiastolicItem {
    return BloodPressureSystolicDiastolicItem(
        systolicBp = systolicBp,
        diastolicBp = diastolicBp
    )
}

fun HCBloodPressureGranularDataSystolicDiastolic.toItem(): BloodPressureGranularDataSystolicDiastolicItem {
    return BloodPressureGranularDataSystolicDiastolicItem(
        dateTime = dateTime,
        systolicBp = bloodPressureSystolicDiastolic.systolicBp,
        diastolicBp = bloodPressureSystolicDiastolic.diastolicBp
    )
}

fun HCHydrationAmountGranularDataMl.toItem(): HydrationAmountGranularDataMlItem {
    return HydrationAmountGranularDataMlItem(
        dateTime = dateTime,
        intervalDurationSeconds = 0,
        hydrationAmountMl = hydrationAmountMl,
    )
}

fun HCTemperatureGranularDataCelsius.toItem(): TemperatureGranularDataCelsiusItem {
    return TemperatureGranularDataCelsiusItem(
        dateTime = dateTime,
        temperatureCelsius = temperatureCelsius,
    )
}