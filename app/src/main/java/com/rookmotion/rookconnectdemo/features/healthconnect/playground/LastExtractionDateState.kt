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
        val bloodGlucoseBodyEventDate: ZonedDateTime,
        val bloodPressureBodyEventDate: ZonedDateTime,
        val bodyMetricsEventDate: ZonedDateTime,
        val heartRateBodyEventDate: ZonedDateTime,
        val heartRatePhysicalEventDate: ZonedDateTime,
        val hydrationBodyDate: ZonedDateTime,
        val moodBodyDate: ZonedDateTime,
        val nutritionBodyEventDate: ZonedDateTime,
        val oxygenationBodyEventDate: ZonedDateTime,
        val oxygenationPhysicalEventDate: ZonedDateTime,
        val stressPhysicalEventDate: ZonedDateTime,
        val temperatureBodyEventDate: ZonedDateTime,
    ) : LastExtractionDateState()
}
