package com.rookmotion.rookconnectdemo.home.health_connect

import java.time.ZonedDateTime

data class HCLastDateState(
    val sleepSummaryDate: ZonedDateTime,
    val physicalSummaryDate: ZonedDateTime,
    val physicalEventsDate: ZonedDateTime,
    val bodySummaryDate: ZonedDateTime
)
