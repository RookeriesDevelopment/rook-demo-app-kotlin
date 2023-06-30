package com.rookmotion.rookconnectdemo.features.healthconnect.playground

import java.time.ZonedDateTime

sealed class LastExtractionDateState {
    object None : LastExtractionDateState()
    class Error(val message: String) : LastExtractionDateState()
    class Success(
        val sleepSummaryDate: ZonedDateTime,
        val physicalSummaryDate: ZonedDateTime,
        val physicalEventDate: ZonedDateTime,
        val bodySummaryDate: ZonedDateTime,
        val bloodGlucoseEventDate: ZonedDateTime,
        val bloodPressureEventDate: ZonedDateTime,
        val bodyMetricsEventDate: ZonedDateTime,
        val heartRateBodyEventDate: ZonedDateTime,
        val heartRatePhysicalEventDate: ZonedDateTime,
        val hydrationEventDate: ZonedDateTime,
        val moodEventDate: ZonedDateTime,
        val nutritionEventDate: ZonedDateTime,
        val oxygenationBodyEventDate: ZonedDateTime,
        val oxygenationPhysicalEventDate: ZonedDateTime,
        val stressEventDate: ZonedDateTime,
        val temperatureEventDate: ZonedDateTime,
    ) : LastExtractionDateState()
}
