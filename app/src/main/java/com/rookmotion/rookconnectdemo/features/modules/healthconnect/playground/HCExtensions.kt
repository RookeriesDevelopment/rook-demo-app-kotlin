package com.rookmotion.rookconnectdemo.features.modules.healthconnect.playground

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
import com.rookmotion.rook.health_connect.domain.model.granular.HCActiveStepsGranularDataStepsPerHr
import com.rookmotion.rook.health_connect.domain.model.granular.HCActivityLevelGranularDataNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCBloodGlucoseGranularDataMgPerDlNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCBloodPressureDaySystolicDiastolicBpNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCBloodPressureGranularDataSystolicDiastolicBpNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCBreathingGranularDataBreathsPerMin
import com.rookmotion.rook.health_connect.domain.model.granular.HCCadenceGranularDataRpm
import com.rookmotion.rook.health_connect.domain.model.granular.HCElevationGranularDataMeters
import com.rookmotion.rook.health_connect.domain.model.granular.HCFloorsClimbedGranularDataFloors
import com.rookmotion.rook.health_connect.domain.model.granular.HCHrGranularDataBpm
import com.rookmotion.rook.health_connect.domain.model.granular.HCHrvRmssdGranularDataNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCHrvSdnnGranularDataNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCHydrationAmountGranularDataMlNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCHydrationLevelGranularDataPercentageNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCLapGranularDataLapsNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCMenstruationFlowMlGranularDataNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCMoodGranularDataScale
import com.rookmotion.rook.health_connect.domain.model.granular.HCPositionGranularDataLatLngDeg
import com.rookmotion.rook.health_connect.domain.model.granular.HCPositionLatLngDeg
import com.rookmotion.rook.health_connect.domain.model.granular.HCPowerGranularDataWattsNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCSaturationGranularDataPercentage
import com.rookmotion.rook.health_connect.domain.model.granular.HCSnoringGranularDataSnores
import com.rookmotion.rook.health_connect.domain.model.granular.HCSpeedGranularDataMetersPerSecond
import com.rookmotion.rook.health_connect.domain.model.granular.HCStepsGranularDataStepsPerHr
import com.rookmotion.rook.health_connect.domain.model.granular.HCStepsGranularDataStepsPerMin
import com.rookmotion.rook.health_connect.domain.model.granular.HCStressGranularDataScoreNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCSwimmingDistanceGranularDataMeters
import com.rookmotion.rook.health_connect.domain.model.granular.HCTemperature
import com.rookmotion.rook.health_connect.domain.model.granular.HCTemperatureGranularDataCelsius
import com.rookmotion.rook.health_connect.domain.model.granular.HCTorqueGranularDataNewtonMeters
import com.rookmotion.rook.health_connect.domain.model.granular.HCTraveledDistanceGranularDataMeters
import com.rookmotion.rook.health_connect.domain.model.granular.HCTssGranularData1To500ScoreNumber
import com.rookmotion.rook.health_connect.domain.model.granular.HCVelocityVectorSpeedAndDirection
import com.rookmotion.rook.health_connect.domain.model.granular.HCVo2GranularDataMlPerMin
import com.rookmotion.rook.health_connect.domain.model.summary.HCBodySummary
import com.rookmotion.rook.health_connect.domain.model.summary.HCPhysicalSummary
import com.rookmotion.rook.health_connect.domain.model.summary.HCSleepSummary
import com.rookmotion.rook.transmission.domain.model.event.BloodGlucoseEventItem
import com.rookmotion.rook.transmission.domain.model.event.BloodPressureEventItem
import com.rookmotion.rook.transmission.domain.model.event.BodyMetricsEventItem
import com.rookmotion.rook.transmission.domain.model.event.HeartRateEventItem
import com.rookmotion.rook.transmission.domain.model.event.HydrationEventItem
import com.rookmotion.rook.transmission.domain.model.event.MoodEventItem
import com.rookmotion.rook.transmission.domain.model.event.NutritionEventItem
import com.rookmotion.rook.transmission.domain.model.event.OxygenationEventItem
import com.rookmotion.rook.transmission.domain.model.event.PhysicalEventItem
import com.rookmotion.rook.transmission.domain.model.event.StressEventItem
import com.rookmotion.rook.transmission.domain.model.event.TemperatureEventItem
import com.rookmotion.rook.transmission.domain.model.granular.ActiveStepsGranularDataStepsPerHrItem
import com.rookmotion.rook.transmission.domain.model.granular.ActivityLevelGranularDataNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.BloodGlucoseGranularDataMgPerDlNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.BloodPressureDaySystolicDiastolicBpNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.BloodPressureGranularDataSystolicDiastolicBpNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.BreathingGranularDataBreathsPerMinItem
import com.rookmotion.rook.transmission.domain.model.granular.CadenceGranularDataRpmItem
import com.rookmotion.rook.transmission.domain.model.granular.ElevationGranularDataMetersItem
import com.rookmotion.rook.transmission.domain.model.granular.FloorsClimbedGranularDataFloorsItem
import com.rookmotion.rook.transmission.domain.model.granular.HrGranularDataBpmItem
import com.rookmotion.rook.transmission.domain.model.granular.HrvRmssdGranularDataNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.HrvSdnnGranularDataNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.HydrationAmountGranularDataMlNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.HydrationLevelGranularDataPercentageNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.LapGranularDataLapsNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.MenstruationFlowMlGranularDataNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.MoodGranularDataScaleItem
import com.rookmotion.rook.transmission.domain.model.granular.PositionGranularDataLatLngDegItem
import com.rookmotion.rook.transmission.domain.model.granular.PositionLatLngDegItem
import com.rookmotion.rook.transmission.domain.model.granular.PowerGranularDataWattsNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.SaturationGranularDataPercentageItem
import com.rookmotion.rook.transmission.domain.model.granular.SnoringGranularDataSnoresItem
import com.rookmotion.rook.transmission.domain.model.granular.SpeedGranularDataMetersPerSecondItem
import com.rookmotion.rook.transmission.domain.model.granular.StepsGranularDataStepsPerHrItem
import com.rookmotion.rook.transmission.domain.model.granular.StepsGranularDataStepsPerMinItem
import com.rookmotion.rook.transmission.domain.model.granular.StressGranularDataScoreNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.SwimmingDistanceGranularDataMetersItem
import com.rookmotion.rook.transmission.domain.model.granular.TemperatureGranularDataCelsiusItem
import com.rookmotion.rook.transmission.domain.model.granular.TemperatureItem
import com.rookmotion.rook.transmission.domain.model.granular.TorqueGranularDataNewtonMetersItem
import com.rookmotion.rook.transmission.domain.model.granular.TraveledDistanceGranularDataMetersItem
import com.rookmotion.rook.transmission.domain.model.granular.TssGranularData1To500ScoreNumberItem
import com.rookmotion.rook.transmission.domain.model.granular.VelocityVectorSpeedAndDirectionItem
import com.rookmotion.rook.transmission.domain.model.granular.Vo2GranularDataMlPerMinItem
import com.rookmotion.rook.transmission.domain.model.summary.BodySummaryItem
import com.rookmotion.rook.transmission.domain.model.summary.PhysicalSummaryItem
import com.rookmotion.rook.transmission.domain.model.summary.SleepSummaryItem

fun HCSleepSummary.toItem(): SleepSummaryItem {
    return SleepSummaryItem(
        dateTime = sleepRelatedData.metadata.dateTime,
        sourceOfData = sleepRelatedData.metadata.sourceOfData,
        sleepStartDateTime = sleepRelatedData.sleepDurationRelatedData.sleepStartDateTime,
        sleepEndDateTime = sleepRelatedData.sleepDurationRelatedData.sleepEndDateTime,
        sleepDate = sleepRelatedData.sleepDurationRelatedData.sleepDate,
        sleepDurationSeconds = sleepRelatedData.sleepDurationRelatedData.sleepDurationSeconds,
        timeInBedSeconds = sleepRelatedData.sleepDurationRelatedData.timeInBedSeconds,
        lightSleepDurationSeconds = sleepRelatedData.sleepDurationRelatedData.lightSleepDurationSeconds,
        remSleepDurationSeconds = sleepRelatedData.sleepDurationRelatedData.remSleepDurationSeconds,
        deepSleepDurationSeconds = sleepRelatedData.sleepDurationRelatedData.deepSleepDurationSeconds,
        timeToFallAsleepSeconds = sleepRelatedData.sleepDurationRelatedData.timeToFallAsleepSeconds,
        timeAwakeDuringSleepSeconds = sleepRelatedData.sleepDurationRelatedData.timeAwakeDuringSleepSeconds,
        sleepQualityRating1To5Score = sleepRelatedData.sleepScores.sleepQualityRating1To5Score,
        sleepEfficiency1To100Score = sleepRelatedData.sleepScores.sleepEfficiency1To100Score,
        sleepGoalSeconds = sleepRelatedData.sleepScores.sleepGoalSeconds,
        sleepContinuity1To5Score = sleepRelatedData.sleepScores.sleepContinuity1To5Score,
        sleepContinuity1To5Rating = sleepRelatedData.sleepScores.sleepContinuity1To5Rating,
        hrMaxBpm = sleepRelatedData.sleepHeartRateRelatedData.hrMaxBpm,
        hrMinimumBpm = sleepRelatedData.sleepHeartRateRelatedData.hrMinimumBpm,
        hrAvgBpm = sleepRelatedData.sleepHeartRateRelatedData.hrAvgBpm,
        hrRestingBpm = sleepRelatedData.sleepHeartRateRelatedData.hrRestingBpm,
        hrBasalBpm = sleepRelatedData.sleepHeartRateRelatedData.hrBasalBpm,
        hrGranularDataBpm = sleepRelatedData.sleepHeartRateRelatedData.hrGranularDataBpm?.map { it.toItem() },
        hrvAvgRmssdNumber = sleepRelatedData.sleepHeartRateRelatedData.hrvAvgRmssdNumber,
        hrvAvgSdnnNumber = sleepRelatedData.sleepHeartRateRelatedData.hrvAvgSdnnNumber,
        hrvSdnnGranularDataNumber = sleepRelatedData.sleepHeartRateRelatedData.hrvSdnnGranularDataNumber?.map { it.toItem() },
        hrvRmssdGranularDataNumber = sleepRelatedData.sleepHeartRateRelatedData.hrvRmssdGranularDataNumber?.map { it.toItem() },
        temperatureMinimumCelsius = sleepRelatedData.temperatureRelatedData.temperatureMinimumCelsius?.toItem(),
        temperatureAvgCelsius = sleepRelatedData.temperatureRelatedData.temperatureAvgCelsius?.toItem(),
        temperatureMaxCelsius = sleepRelatedData.temperatureRelatedData.temperatureMaxCelsius?.toItem(),
        temperatureGranularDataCelsius = sleepRelatedData.temperatureRelatedData.temperatureGranularDataCelsius?.map { it.toItem() },
        temperatureDeltaCelsius = sleepRelatedData.temperatureRelatedData.temperatureDeltaCelsius?.toItem(),
        breathsMinimumPerMin = sleepRelatedData.breathingRelatedData.breathsMinimumPerMin,
        breathsAvgPerMin = sleepRelatedData.breathingRelatedData.breathsAvgPerMin,
        breathsMaxPerMin = sleepRelatedData.breathingRelatedData.breathsMaxPerMin,
        breathingGranularDataBreathsPerMin = sleepRelatedData.breathingRelatedData.breathingGranularDataBreathsPerMin?.map { it.toItem() },
        snoringEventsCountNumber = sleepRelatedData.breathingRelatedData.snoringEventsCountNumber,
        snoringDurationTotalSeconds = sleepRelatedData.breathingRelatedData.snoringDurationTotalSeconds,
        snoringGranularDataSnores = sleepRelatedData.breathingRelatedData.snoringGranularDataSnores?.map { it.toItem() },
        saturationAvgPercentage = sleepRelatedData.breathingRelatedData.saturationAvgPercentage,
        saturationMinPercentage = sleepRelatedData.breathingRelatedData.saturationMinPercentage,
        saturationMaxPercentage = sleepRelatedData.breathingRelatedData.saturationMaxPercentage,
        saturationGranularDataPercentage = sleepRelatedData.breathingRelatedData.saturationGranularDataPercentage?.map { it.toItem() },
    )
}

fun HCPhysicalSummary.toItem(): PhysicalSummaryItem {
    return PhysicalSummaryItem(
        dateTime = dailyActivityRelatedData.metadata.dateTime,
        sourceOfData = dailyActivityRelatedData.metadata.sourceOfData,
        stepsPerDayNumber = dailyActivityRelatedData.distanceData.stepsPerDayNumber,
        stepsGranularDataStepsPerHr = dailyActivityRelatedData.distanceData.stepsGranularDataStepsPerHr?.map { it.toItem() },
        activeStepsPerDayNumber = dailyActivityRelatedData.distanceData.activeStepsPerDayNumber,
        activeStepsGranularDataStepsPerHr = dailyActivityRelatedData.distanceData.activeStepsGranularDataStepsPerHr?.map { it.toItem() },
        walkedDistanceMeters = dailyActivityRelatedData.distanceData.walkedDistanceMeters,
        traveledDistanceMeters = dailyActivityRelatedData.distanceData.traveledDistanceMeters,
        traveledDistanceGranularDataMeters = dailyActivityRelatedData.distanceData.traveledDistanceGranularDataMeters?.map { it.toItem() },
        floorsClimbedNumber = dailyActivityRelatedData.distanceData.floorsClimbedNumber,
        floorsClimbedGranularDataFloors = dailyActivityRelatedData.distanceData.floorsClimbedGranularDataFloors?.map { it.toItem() },
        elevationAvgAltitudeMeters = dailyActivityRelatedData.distanceData.elevationAvgAltitudeMeters,
        elevationMinimumAltitudeMeters = dailyActivityRelatedData.distanceData.elevationMinimumAltitudeMeters,
        elevationMaxAltitudeMeters = dailyActivityRelatedData.distanceData.elevationMaxAltitudeMeters,
        elevationLossActualAltitudeMeters = dailyActivityRelatedData.distanceData.elevationLossActualAltitudeMeters,
        elevationGainActualAltitudeMeters = dailyActivityRelatedData.distanceData.elevationGainActualAltitudeMeters,
        elevationPlannedGainMeters = dailyActivityRelatedData.distanceData.elevationPlannedGainMeters,
        elevationGranularDataMeters = dailyActivityRelatedData.distanceData.elevationGranularDataMeters?.map { it.toItem() },
        swimmingStrokesNumber = dailyActivityRelatedData.distanceData.swimmingStrokesNumber,
        swimmingNumLapsNumber = dailyActivityRelatedData.distanceData.swimmingNumLapsNumber,
        swimmingPoolLengthMeters = dailyActivityRelatedData.distanceData.swimmingPoolLengthMeters,
        swimmingTotalDistanceMeters = dailyActivityRelatedData.distanceData.swimmingTotalDistanceMeters,
        swimmingDistanceGranularDataMeters = dailyActivityRelatedData.distanceData.swimmingDistanceGranularDataMeters?.map { it.toItem() },
        saturationAvgPercentage = dailyActivityRelatedData.oxygenationData.saturationAvgPercentage,
        saturationGranularDataPercentage = dailyActivityRelatedData.oxygenationData.saturationGranularDataPercentage?.map { it.toItem() },
        vo2MaxMlPerMinPerKg = dailyActivityRelatedData.oxygenationData.vo2MaxMlPerMinPerKg,
        vo2GranularDataMlPerMin = dailyActivityRelatedData.oxygenationData.vo2GranularDataMlPerMin?.map { it.toItem() },
        activeSeconds = dailyActivityRelatedData.activityData.activeSeconds,
        restSeconds = dailyActivityRelatedData.activityData.restSeconds,
        lowIntensitySeconds = dailyActivityRelatedData.activityData.lowIntensitySeconds,
        moderateIntensitySeconds = dailyActivityRelatedData.activityData.moderateIntensitySeconds,
        vigorousIntensitySeconds = dailyActivityRelatedData.activityData.vigorousIntensitySeconds,
        inactivitySeconds = dailyActivityRelatedData.activityData.inactivitySeconds,
        activityLevelGranularDataNumber = dailyActivityRelatedData.activityData.activityLevelGranularDataNumber?.map { it.toItem() },
        continuousInactivePeriodsNumber = dailyActivityRelatedData.activityData.continuousInactivePeriodsNumber,
        caloriesNetIntakeKilocalories = dailyActivityRelatedData.caloriesData.caloriesNetIntakeKilocalories,
        caloriesExpenditureKilocalories = dailyActivityRelatedData.caloriesData.caloriesExpenditureKilocalories,
        caloriesNetActiveKilocalories = dailyActivityRelatedData.caloriesData.caloriesNetActiveKilocalories,
        caloriesBasalMetabolicRateKilocalories = dailyActivityRelatedData.caloriesData.caloriesBasalMetabolicRateKilocalories,
        hrMaxBpm = dailyActivityRelatedData.heartRateData.hrMaxBpm,
        hrMinimumBpm = dailyActivityRelatedData.heartRateData.hrMinimumBpm,
        hrAvgBpm = dailyActivityRelatedData.heartRateData.hrAvgBpm,
        hrRestingBpm = dailyActivityRelatedData.heartRateData.hrRestingBpm,
        hrGranularDataBpm = dailyActivityRelatedData.heartRateData.hrGranularDataBpm?.map { it.toItem() },
        hrvAvgRmssdNumber = dailyActivityRelatedData.heartRateData.hrvAvgRmssdNumber,
        hrvAvgSdnnNumber = dailyActivityRelatedData.heartRateData.hrvAvgSdnnNumber,
        hrvSdnnGranularDataNumber = dailyActivityRelatedData.heartRateData.hrvSdnnGranularDataNumber?.map { it.toItem() },
        hrvRmssdGranularDataNumber = dailyActivityRelatedData.heartRateData.hrvRmssdGranularDataNumber?.map { it.toItem() },
        stressAtRestDurationSeconds = dailyActivityRelatedData.stressData.stressAtRestDurationSeconds,
        stressDurationSeconds = dailyActivityRelatedData.stressData.stressDurationSeconds,
        lowStressDurationSeconds = dailyActivityRelatedData.stressData.lowStressDurationSeconds,
        mediumStressDurationSeconds = dailyActivityRelatedData.stressData.mediumStressDurationSeconds,
        highStressDurationSeconds = dailyActivityRelatedData.stressData.highStressDurationSeconds,
        stressGranularDataScoreNumber = dailyActivityRelatedData.stressData.stressGranularDataScoreNumber?.map { it.toItem() },
        stressAvgLevelNumber = dailyActivityRelatedData.stressData.stressAvgLevelNumber,
        stressMaxLevelNumber = dailyActivityRelatedData.stressData.stressMaxLevelNumber,
    )
}

fun HCPhysicalEvent.toItem(): PhysicalEventItem {
    return PhysicalEventItem(
        dateTime = activity.metadata.dateTime,
        sourceOfData = activity.metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = activity.metadata.wasTheUserUnderPhysicalActivity,
        activityStartTimeDateTime = activity.activityData.activityStartTimeDateTime,
        activityEndTimeDateTime = activity.activityData.activityEndTimeDateTime,
        activityDurationSeconds = activity.activityData.activityDurationSeconds,
        activityTypeName = activity.activityData.activityTypeName,
        activeSeconds = activity.activityData.activeSeconds,
        restSeconds = activity.activityData.restSeconds,
        lowIntensitySeconds = activity.activityData.lowIntensitySeconds,
        moderateIntensitySeconds = activity.activityData.moderateIntensitySeconds,
        vigorousIntensitySeconds = activity.activityData.vigorousIntensitySeconds,
        inactivitySeconds = activity.activityData.inactivitySeconds,
        activityLevelGranularDataNumber = activity.activityData.activityLevelGranularDataNumber?.map { it.toItem() },
        continuousInactivePeriodsNumber = activity.activityData.continuousInactivePeriodsNumber,
        activityStrainLevelNumber = activity.activityData.activityStrainLevelNumber,
        activityWorkKilojoules = activity.activityData.activityWorkKilojoules,
        activityEnergyKilojoules = activity.activityData.activityEnergyKilojoules,
        activityEnergyPlannedKilojoules = activity.activityData.activityEnergyPlannedKilojoules,
        caloriesNetIntakeKilocalories = activity.caloriesData.caloriesNetIntakeKilocalories,
        caloriesExpenditureKilocalories = activity.caloriesData.caloriesExpenditureKilocalories,
        caloriesNetActiveKilocalories = activity.caloriesData.caloriesNetActiveKilocalories,
        caloriesBasalMetabolicRateKilocalories = activity.caloriesData.caloriesBasalMetabolicRateKilocalories,
        fatPercentageOfCaloriesPercentage = activity.caloriesData.fatPercentageOfCaloriesPercentage,
        carbohydratePercentageOfCaloriesPercentage = activity.caloriesData.carbohydratePercentageOfCaloriesPercentage,
        proteinPercentageOfCaloriesPercentage = activity.caloriesData.proteinPercentageOfCaloriesPercentage,
        stepsNumber = activity.distanceData.stepsNumber,
        stepsGranularDataStepsPerMin = activity.distanceData.stepsGranularDataStepsPerMin?.map { it.toItem() },
        walkedDistanceMeters = activity.distanceData.walkedDistanceMeters,
        traveledDistanceMeters = activity.distanceData.traveledDistanceMeters,
        traveledDistanceGranularDataMeters = activity.distanceData.traveledDistanceGranularDataMeters?.map { it.toItem() },
        floorsClimbedNumber = activity.distanceData.floorsClimbedNumber,
        floorsClimbedGranularDataFloorsNumber = activity.distanceData.floorsClimbedGranularDataFloorsNumber?.map { it.toItem() },
        elevationAvgAltitudeMeters = activity.distanceData.elevationAvgAltitudeMeters,
        elevationMinimumAltitudeMeters = activity.distanceData.elevationMinimumAltitudeMeters,
        elevationMaxAltitudeMeters = activity.distanceData.elevationMaxAltitudeMeters,
        elevationLossActualAltitudeMeters = activity.distanceData.elevationLossActualAltitudeMeters,
        elevationGainActualAltitudeMeters = activity.distanceData.elevationGainActualAltitudeMeters,
        elevationPlannedGainMeters = activity.distanceData.elevationPlannedGainMeters,
        elevationGranularDataMeters = activity.distanceData.elevationGranularDataMeters?.map { it.toItem() },
        swimmingNumStrokesNumber = activity.distanceData.swimmingNumStrokesNumber,
        swimmingNumLapsNumber = activity.distanceData.swimmingNumLapsNumber,
        swimmingPoolLengthMeters = activity.distanceData.swimmingPoolLengthMeters,
        swimmingTotalDistanceMeters = activity.distanceData.swimmingTotalDistanceMeters,
        swimmingDistanceGranularDataMeters = activity.distanceData.swimmingDistanceGranularDataMeters?.map { it.toItem() },
        hrMaxBpm = activity.heartRateData.hrMaxBpm,
        hrMinimumBpm = activity.heartRateData.hrMinimumBpm,
        hrAvgBpm = activity.heartRateData.hrAvgBpm,
        hrRestingBpm = activity.heartRateData.hrRestingBpm,
        hrGranularDataBpm = activity.heartRateData.hrGranularDataBpm?.map { it.toItem() },
        hrvAvgRmssdNumber = activity.heartRateData.hrvAvgRmssdNumber,
        hrvAvgSdnnNumber = activity.heartRateData.hrvAvgSdnnNumber,
        hrvSdnnGranularDataNumber = activity.heartRateData.hrvSdnnGranularDataNumber?.map { it.toItem() },
        hrvRmssdGranularDataNumber = activity.heartRateData.hrvRmssdGranularDataNumber?.map { it.toItem() },
        speedNormalizedMetersPerSecond = activity.movementData.speedNormalizedMetersPerSecond,
        speedAvgMetersPerSecond = activity.movementData.speedAvgMetersPerSecond,
        speedMaxMetersPerSecond = activity.movementData.speedMaxMetersPerSecond,
        speedGranularDataMetersPerSecond = activity.movementData.speedGranularDataMetersPerSecond?.map { it.toItem() },
        velocityVectorAvgSpeedAndDirection = activity.movementData.velocityVectorAvgSpeedAndDirection?.toItem(),
        velocityVectorMaxSpeedAndDirection = activity.movementData.velocityVectorMaxSpeedAndDirection?.toItem(),
        paceAvgMinutesPerKilometer = activity.movementData.paceAvgMinutesPerKilometer,
        paceMaxMinutesPerKilometer = activity.movementData.paceMaxMinutesPerKilometer,
        cadenceAvgRpm = activity.movementData.cadenceAvgRpm,
        cadenceMaxRpm = activity.movementData.cadenceMaxRpm,
        cadenceGranularDataRpm = activity.movementData.cadenceGranularDataRpm?.map { it.toItem() },
        torqueAvgNewtonMeters = activity.movementData.torqueAvgNewtonMeters,
        torqueMaxNewtonMeters = activity.movementData.torqueMaxNewtonMeters,
        torqueGranularDataNewtonMeters = activity.movementData.torqueGranularDataNewtonMeters?.map { it.toItem() },
        lapGranularDataLapsNumber = activity.movementData.lapGranularDataLapsNumber?.map { it.toItem() },
        powerAvgWattsNumber = activity.powerData.powerAvgWattsNumber,
        powerMaxWattsNumber = activity.powerData.powerMaxWattsNumber,
        powerGranularDataWattsNumber = activity.powerData.powerGranularDataWattsNumber?.map { it.toItem() },
        positionStartLatLngDeg = activity.positionData.positionStartLatLngDeg?.toItem(),
        positionCentroidLatLngDeg = activity.positionData.positionCentroidLatLngDeg?.toItem(),
        positionEndLatLngDeg = activity.positionData.positionEndLatLngDeg?.toItem(),
        positionGranularDataLatLngDeg = activity.positionData.positionGranularDataLatLngDeg?.map { it.toItem() },
        positionPolylineMapDataSummaryString = activity.positionData.positionPolylineMapDataSummaryString,
        saturationAvgPercentage = activity.oxygenationData.saturationAvgPercentage,
        saturationGranularDataPercentage = activity.oxygenationData.saturationGranularDataPercentage?.map { it.toItem() },
        vo2MaxMlPerMinPerKg = activity.oxygenationData.vo2MaxMlPerMinPerKg,
        vo2GranularDataMlPerMin = activity.oxygenationData.vo2GranularDataMlPerMin?.map { it.toItem() },
        stressAtRestDurationSeconds = activity.stressData.stressAtRestDurationSeconds,
        stressDurationSeconds = activity.stressData.stressDurationSeconds,
        lowStressDurationSeconds = activity.stressData.lowStressDurationSeconds,
        mediumStressDurationSeconds = activity.stressData.mediumStressDurationSeconds,
        highStressDurationSeconds = activity.stressData.highStressDurationSeconds,
        stressAvgLevelNumber = activity.stressData.stressAvgLevelNumber,
        stressMaxLevelNumber = activity.stressData.stressMaxLevelNumber,
        tssGranularData1To500ScoreNumber = activity.stressData.tssGranularData1To500ScoreNumber?.map { it.toItem() },
    )
}

fun HCBodySummary.toItem(): BodySummaryItem {
    return BodySummaryItem(
        dateTime = bodyData.metadata.dateTime,
        sourceOfData = bodyData.metadata.sourceOfData,
        waistCircumferenceCmNumber = bodyData.bodyMetrics.waistCircumferenceCmNumber,
        hipCircumferenceCmNumber = bodyData.bodyMetrics.hipCircumferenceCmNumber,
        chestCircumferenceCmNumber = bodyData.bodyMetrics.chestCircumferenceCmNumber,
        boneCompositionPercentageNumber = bodyData.bodyMetrics.boneCompositionPercentageNumber,
        muscleCompositionPercentageNumber = bodyData.bodyMetrics.muscleCompositionPercentageNumber,
        waterCompositionPercentageNumber = bodyData.bodyMetrics.waterCompositionPercentageNumber,
        weightKgNumber = bodyData.bodyMetrics.weightKgNumber,
        heightCmNumber = bodyData.bodyMetrics.heightCmNumber,
        bmiNumber = bodyData.bodyMetrics.bmiNumber,
        bloodGlucoseDayAvgMgPerDlNumber = bodyData.bloodGlucose.bloodGlucoseDayAvgMgPerDlNumber,
        bloodGlucoseGranularDataMgPerDlNumber = bodyData.bloodGlucose.bloodGlucoseGranularDataMgPerDlNumber?.map { it.toItem() },
        bloodPressureDayAvgSystolicDiastolicBpNumber = bodyData.bloodPressure.bloodPressureDayAvgSystolicDiastolicBpNumber?.toItem(),
        bloodPressureGranularDataSystolicDiastolicBpNumber = bodyData.bloodPressure.bloodPressureGranularDataSystolicDiastolicBpNumber?.map { it.toItem() },
        waterTotalConsumptionMlNumber = bodyData.hydration.waterTotalConsumptionMlNumber,
        hydrationAmountGranularDataMlNumber = bodyData.hydration.hydrationAmountGranularDataMlNumber?.map { it.toItem() },
        hydrationLevelGranularDataPercentageNumber = bodyData.hydration.hydrationLevelGranularDataPercentageNumber?.map { it.toItem() },
        hrMaxBpm = bodyData.heartRateData.hrMaxBpm,
        hrMinimumBpm = bodyData.heartRateData.hrMinimumBpm,
        hrAvgBpm = bodyData.heartRateData.hrAvgBpm,
        hrRestingBpm = bodyData.heartRateData.hrRestingBpm,
        hrGranularDataBpm = bodyData.heartRateData.hrGranularDataBpm?.map { it.toItem() },
        hrvAvgRmssdNumber = bodyData.heartRateData.hrvAvgRmssdNumber,
        hrvAvgSdnnNumber = bodyData.heartRateData.hrvAvgSdnnNumber,
        hrvSdnnGranularDataNumber = bodyData.heartRateData.hrvSdnnGranularDataNumber?.map { it.toItem() },
        hrvRmssdGranularDataNumber = bodyData.heartRateData.hrvRmssdGranularDataNumber?.map { it.toItem() },
        moodMinimumScale = bodyData.mood.moodMinimumScale,
        moodAvgScale = bodyData.mood.moodAvgScale,
        moodMaxScale = bodyData.mood.moodMaxScale,
        moodDeltaScale = bodyData.mood.moodDeltaScale,
        moodGranularDataScale = bodyData.mood.moodGranularDataScale?.map { it.toItem() },
        foodIntakeNumber = bodyData.nutrition.foodIntakeNumber,
        caloriesIntakeNumber = bodyData.nutrition.caloriesIntakeNumber,
        proteinIntakeGNumber = bodyData.nutrition.proteinIntakeGNumber,
        sugarIntakeGNumber = bodyData.nutrition.sugarIntakeGNumber,
        fatIntakeGNumber = bodyData.nutrition.fatIntakeGNumber,
        transFatIntakeGNumber = bodyData.nutrition.transFatIntakeGNumber,
        carbohydratesIntakeGNumber = bodyData.nutrition.carbohydratesIntakeGNumber,
        fiberIntakeGNumber = bodyData.nutrition.fiberIntakeGNumber,
        alcoholIntakeGNumber = bodyData.nutrition.alcoholIntakeGNumber,
        sodiumIntakeMgNumber = bodyData.nutrition.sodiumIntakeMgNumber,
        cholesterolIntakeMgNumber = bodyData.nutrition.cholesterolIntakeMgNumber,
        saturationAvgPercentage = bodyData.oxygenationData.saturationAvgPercentage,
        saturationGranularDataPercentage = bodyData.oxygenationData.saturationGranularDataPercentage?.map { it.toItem() },
        vo2MaxMlPerMinPerKg = bodyData.oxygenationData.vo2MaxMlPerMinPerKg,
        vo2GranularDataMlPerMin = bodyData.oxygenationData.vo2GranularDataMlPerMin?.map { it.toItem() },
        temperatureMinimumCelsius = bodyData.temperatureData.temperatureMinimumCelsius?.toItem(),
        temperatureAvgCelsius = bodyData.temperatureData.temperatureAvgCelsius?.toItem(),
        temperatureMaxCelsius = bodyData.temperatureData.temperatureMaxCelsius?.toItem(),
        temperatureGranularDataCelsius = bodyData.temperatureData.temperatureGranularDataCelsius?.map { it.toItem() },
        temperatureDeltaCelsius = bodyData.temperatureData.temperatureDeltaCelsius?.toItem(),
        lastUpdatedDatetime = bodyData.menstruationData.lastUpdatedDatetime,
        periodStartDate = bodyData.menstruationData.periodStartDate,
        cycleDay = bodyData.menstruationData.cycleDay,
        cycleLengthDays = bodyData.menstruationData.cycleLengthDays,
        predictedCycleLengthDays = bodyData.menstruationData.predictedCycleLengthDays,
        currentPhase = bodyData.menstruationData.currentPhase,
        lengthOfCurrentPhaseDays = bodyData.menstruationData.lengthOfCurrentPhaseDays,
        daysUntilNextPhase = bodyData.menstruationData.daysUntilNextPhase,
        isAPredictedCycle = bodyData.menstruationData.isAPredictedCycle,
        menstruationFlowMlGranularDataNumber = bodyData.menstruationData.menstruationFlowMlGranularDataNumber?.map { it.toItem() },
    )
}

fun HCBloodGlucoseEvent.toItem(): BloodGlucoseEventItem {
    return BloodGlucoseEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        bloodGlucoseDayAvgMgPerDlNumber = bloodGlucose.bloodGlucoseDayAvgMgPerDlNumber,
        bloodGlucoseGranularDataMgPerDlNumber = bloodGlucose.bloodGlucoseGranularDataMgPerDlNumber?.map { it.toItem() },
    )
}

fun HCBloodPressureEvent.toItem(): BloodPressureEventItem {
    return BloodPressureEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        bloodPressureDayAvgSystolicDiastolicBpNumber = bloodPressure.bloodPressureDayAvgSystolicDiastolicBpNumber?.toItem(),
        bloodPressureGranularDataSystolicDiastolicBpNumber = bloodPressure.bloodPressureGranularDataSystolicDiastolicBpNumber?.map { it.toItem() },
    )
}

fun HCBodyMetricsEvent.toItem(): BodyMetricsEventItem {
    return BodyMetricsEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        waistCircumferenceCmNumber = bodyMetrics.waistCircumferenceCmNumber,
        hipCircumferenceCmNumber = bodyMetrics.hipCircumferenceCmNumber,
        chestCircumferenceCmNumber = bodyMetrics.chestCircumferenceCmNumber,
        boneCompositionPercentageNumber = bodyMetrics.boneCompositionPercentageNumber,
        muscleCompositionPercentageNumber = bodyMetrics.muscleCompositionPercentageNumber,
        waterCompositionPercentageNumber = bodyMetrics.waterCompositionPercentageNumber,
        weightKgNumber = bodyMetrics.weightKgNumber,
        heightCmNumber = bodyMetrics.heightCmNumber,
        bmiNumber = bodyMetrics.bmiNumber,
    )
}

fun HCHeartRateEvent.toItem(): HeartRateEventItem {
    return HeartRateEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        hrMaxBpm = heartRateData.hrMaxBpm,
        hrMinimumBpm = heartRateData.hrMinimumBpm,
        hrAvgBpm = heartRateData.hrAvgBpm,
        hrRestingBpm = heartRateData.hrRestingBpm,
        hrGranularDataBpm = heartRateData.hrGranularDataBpm?.map { it.toItem() },
        hrvAvgRmssdNumber = heartRateData.hrvAvgRmssdNumber,
        hrvAvgSdnnNumber = heartRateData.hrvAvgSdnnNumber,
        hrvSdnnGranularDataNumber = heartRateData.hrvSdnnGranularDataNumber?.map { it.toItem() },
        hrvRmssdGranularDataNumber = heartRateData.hrvRmssdGranularDataNumber?.map { it.toItem() },
    )
}

fun HCHydrationEvent.toItem(): HydrationEventItem {
    return HydrationEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        waterTotalConsumptionMlNumber = hydration.waterTotalConsumptionMlNumber,
        hydrationAmountGranularDataMlNumber = hydration.hydrationAmountGranularDataMlNumber?.map { it.toItem() },
        hydrationLevelGranularDataPercentageNumber = hydration.hydrationLevelGranularDataPercentageNumber?.map { it.toItem() },
    )
}

fun HCMoodEvent.toItem(): MoodEventItem {
    return MoodEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        moodMinimumScale = mood.moodMinimumScale,
        moodAvgScale = mood.moodAvgScale,
        moodMaxScale = mood.moodMaxScale,
        moodGranularDataScale = mood.moodGranularDataScale?.map { it.toItem() },
        moodDeltaScale = mood.moodDeltaScale,
    )
}

fun HCNutritionEvent.toItem(): NutritionEventItem {
    return NutritionEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        foodIntakeNumber = nutrition.foodIntakeNumber,
        caloriesIntakeNumber = nutrition.caloriesIntakeNumber,
        proteinIntakeGNumber = nutrition.proteinIntakeGNumber,
        sugarIntakeGNumber = nutrition.sugarIntakeGNumber,
        fatIntakeGNumber = nutrition.fatIntakeGNumber,
        transFatIntakeGNumber = nutrition.transFatIntakeGNumber,
        carbohydratesIntakeGNumber = nutrition.carbohydratesIntakeGNumber,
        fiberIntakeGNumber = nutrition.fiberIntakeGNumber,
        alcoholIntakeGNumber = nutrition.alcoholIntakeGNumber,
        sodiumIntakeMgNumber = nutrition.sodiumIntakeMgNumber,
        cholesterolIntakeMgNumber = nutrition.cholesterolIntakeMgNumber,
    )
}

fun HCOxygenationEvent.toItem(): OxygenationEventItem {
    return OxygenationEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        saturationAvgPercentage = oxygenationData.saturationAvgPercentage,
        saturationGranularDataPercentage = oxygenationData.saturationGranularDataPercentage?.map { it.toItem() },
        vo2MaxMlPerMinPerKg = oxygenationData.vo2MaxMlPerMinPerKg,
        vo2GranularDataMlPerMin = oxygenationData.vo2GranularDataMlPerMin?.map { it.toItem() },
    )
}

fun HCStressEvent.toItem(): StressEventItem {
    return StressEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        stressAtRestDurationSeconds = stressData.stressAtRestDurationSeconds,
        stressDurationSeconds = stressData.stressDurationSeconds,
        lowStressDurationSeconds = stressData.lowStressDurationSeconds,
        mediumStressDurationSeconds = stressData.mediumStressDurationSeconds,
        highStressDurationSeconds = stressData.highStressDurationSeconds,
        tssGranularData1To500ScoreNumber = stressData.tssGranularData1To500ScoreNumber?.map { it.toItem() },
        stressAvgLevelNumber = stressData.stressAvgLevelNumber,
        stressMaxLevelNumber = stressData.stressMaxLevelNumber,
    )
}

fun HCTemperatureEvent.toItem(): TemperatureEventItem {
    return TemperatureEventItem(
        dateTime = metadata.dateTime,
        sourceOfData = metadata.sourceOfData,
        wasTheUserUnderPhysicalActivity = metadata.wasTheUserUnderPhysicalActivity,
        temperatureMinimumCelsius = temperatureData.temperatureMinimumCelsius?.toItem(),
        temperatureAvgCelsius = temperatureData.temperatureAvgCelsius?.toItem(),
        temperatureMaxCelsius = temperatureData.temperatureMaxCelsius?.toItem(),
        temperatureGranularDataCelsius = temperatureData.temperatureGranularDataCelsius?.map { it.toItem() },
        temperatureDeltaCelsius = temperatureData.temperatureDeltaCelsius?.toItem(),
    )
}

fun HCActiveStepsGranularDataStepsPerHr.toItem(): ActiveStepsGranularDataStepsPerHrItem {
    return ActiveStepsGranularDataStepsPerHrItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        steps = steps,
    )
}

fun HCActivityLevelGranularDataNumber.toItem(): ActivityLevelGranularDataNumberItem {
    return ActivityLevelGranularDataNumberItem(
        dateTime = dateTime,
        activityLevel = activityLevel,
    )
}


fun HCBloodGlucoseGranularDataMgPerDlNumber.toItem(): BloodGlucoseGranularDataMgPerDlNumberItem {
    return BloodGlucoseGranularDataMgPerDlNumberItem(
        dateTime = dateTime,
        bloodGlucoseMgPerDlNumber = bloodGlucoseMgPerDlNumber,
    )
}

fun HCBloodPressureDaySystolicDiastolicBpNumber.toItem(): BloodPressureDaySystolicDiastolicBpNumberItem {
    return BloodPressureDaySystolicDiastolicBpNumberItem(
        systolicBp = systolicBp,
        diastolicBp = diastolicBp,
    )
}

fun HCBloodPressureGranularDataSystolicDiastolicBpNumber.toItem(): BloodPressureGranularDataSystolicDiastolicBpNumberItem {
    return BloodPressureGranularDataSystolicDiastolicBpNumberItem(
        dateTime = dateTime,
        systolicBp = systolicBp,
        diastolicBp = diastolicBp
    )
}

fun HCBreathingGranularDataBreathsPerMin.toItem(): BreathingGranularDataBreathsPerMinItem {
    return BreathingGranularDataBreathsPerMinItem(
        dateTime = dateTime,
        breathsPerMin = breathsPerMin,
    )
}

fun HCCadenceGranularDataRpm.toItem(): CadenceGranularDataRpmItem {
    return CadenceGranularDataRpmItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        cadenceRpm = cadenceRpm,
    )
}

fun HCElevationGranularDataMeters.toItem(): ElevationGranularDataMetersItem {
    return ElevationGranularDataMetersItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        elevationChange = elevationChange,
    )
}

fun HCFloorsClimbedGranularDataFloors.toItem(): FloorsClimbedGranularDataFloorsItem {
    return FloorsClimbedGranularDataFloorsItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        floorsClimbed = floorsClimbed,
    )
}


fun HCHrGranularDataBpm.toItem(): HrGranularDataBpmItem {
    return HrGranularDataBpmItem(
        dateTime = dateTime,
        hrBpm = hrBpm,
    )
}

fun HCHrvRmssdGranularDataNumber.toItem(): HrvRmssdGranularDataNumberItem {
    return HrvRmssdGranularDataNumberItem(
        dateTime = dateTime,
        hrvRmssd = hrvRmssdNumber,
    )
}

fun HCHrvSdnnGranularDataNumber.toItem(): HrvSdnnGranularDataNumberItem {
    return HrvSdnnGranularDataNumberItem(
        dateTime = dateTime,
        hrvSdnn = hrvSdnnNumber,
    )
}

fun HCHydrationAmountGranularDataMlNumber.toItem(): HydrationAmountGranularDataMlNumberItem {
    return HydrationAmountGranularDataMlNumberItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        hydrationAmountMl = hydrationAmountMl,
    )
}

fun HCHydrationLevelGranularDataPercentageNumber.toItem(): HydrationLevelGranularDataPercentageNumberItem {
    return HydrationLevelGranularDataPercentageNumberItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        hydrationLevelPercentage = hydrationLevelPercentage,
    )
}

fun HCLapGranularDataLapsNumber.toItem(): LapGranularDataLapsNumberItem {
    return LapGranularDataLapsNumberItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        laps = laps,
    )
}

fun HCMenstruationFlowMlGranularDataNumber.toItem(): MenstruationFlowMlGranularDataNumberItem {
    return MenstruationFlowMlGranularDataNumberItem(
        dateTime = dateTime,
        flowMl = flowMl,
    )
}

fun HCMoodGranularDataScale.toItem(): MoodGranularDataScaleItem {
    return MoodGranularDataScaleItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        moodScale = moodScale,
    )
}

fun HCPositionGranularDataLatLngDeg.toItem(): PositionGranularDataLatLngDegItem {
    return PositionGranularDataLatLngDegItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        lat = lat,
        lng = lng,
    )
}

fun HCPositionLatLngDeg.toItem(): PositionLatLngDegItem {
    return PositionLatLngDegItem(
        lat = lat,
        lng = lng,
    )
}

fun HCPowerGranularDataWattsNumber.toItem(): PowerGranularDataWattsNumberItem {
    return PowerGranularDataWattsNumberItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        powerWatts = powerWatts,
    )
}

fun HCSaturationGranularDataPercentage.toItem(): SaturationGranularDataPercentageItem {
    return SaturationGranularDataPercentageItem(
        dateTime = dateTime,
        saturationPercentage = saturationPercentage,
    )
}

fun HCSnoringGranularDataSnores.toItem(): SnoringGranularDataSnoresItem {
    return SnoringGranularDataSnoresItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        snoringEventsCountNumber = snoringEventsCountNumber,
    )
}

fun HCSpeedGranularDataMetersPerSecond.toItem(): SpeedGranularDataMetersPerSecondItem {
    return SpeedGranularDataMetersPerSecondItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        speedMetersPerSecond = speedMetersPerSecond,
    )
}

fun HCStepsGranularDataStepsPerHr.toItem(): StepsGranularDataStepsPerHrItem {
    return StepsGranularDataStepsPerHrItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        steps = steps,
    )
}

fun HCStepsGranularDataStepsPerMin.toItem(): StepsGranularDataStepsPerMinItem {
    return StepsGranularDataStepsPerMinItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        steps = steps,
    )
}

fun HCStressGranularDataScoreNumber.toItem(): StressGranularDataScoreNumberItem {
    return StressGranularDataScoreNumberItem(
        dateTime = dateTime,
        stressScore = stressScore,
    )
}


fun HCSwimmingDistanceGranularDataMeters.toItem(): SwimmingDistanceGranularDataMetersItem {
    return SwimmingDistanceGranularDataMetersItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        swimmingDistanceMeters = swimmingDistanceMeters,
    )
}

fun HCTemperatureGranularDataCelsius.toItem(): TemperatureGranularDataCelsiusItem {
    return TemperatureGranularDataCelsiusItem(
        dateTime = dateTime,
        temperatureCelsius = temperatureCelsius,
        measurementType = measurementType,
    )
}

fun HCTemperature.toItem(): TemperatureItem {
    return TemperatureItem(
        temperatureCelsius = temperatureCelsius,
        measurementType = measurementType,
    )
}

fun HCTorqueGranularDataNewtonMeters.toItem(): TorqueGranularDataNewtonMetersItem {
    return TorqueGranularDataNewtonMetersItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        torqueNewtonMeters = torqueNewtonMeters,
    )
}

fun HCTraveledDistanceGranularDataMeters.toItem(): TraveledDistanceGranularDataMetersItem {
    return TraveledDistanceGranularDataMetersItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        traveledDistanceMeters = traveledDistanceMeters,
    )
}

fun HCTssGranularData1To500ScoreNumber.toItem(): TssGranularData1To500ScoreNumberItem {
    return TssGranularData1To500ScoreNumberItem(
        dateTime = dateTime,
        intervalDurationSeconds = intervalDurationSeconds,
        tss1To500Score = tss1To500Score,
    )
}

fun HCVelocityVectorSpeedAndDirection.toItem(): VelocityVectorSpeedAndDirectionItem {
    return VelocityVectorSpeedAndDirectionItem(
        speedMetersPerSecond = speedMetersPerSecond,
        direction = direction,
    )
}

fun HCVo2GranularDataMlPerMin.toItem(): Vo2GranularDataMlPerMinItem {
    return Vo2GranularDataMlPerMinItem(
        dateTime = dateTime,
        vo2MlPerMin = vo2MlPerMin,
    )
}
