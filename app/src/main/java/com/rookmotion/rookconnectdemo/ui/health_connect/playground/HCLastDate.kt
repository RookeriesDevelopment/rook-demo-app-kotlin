package com.rookmotion.rookconnectdemo.ui.health_connect.playground

import java.time.ZonedDateTime

data class HCLastDate(
    val sleepSummaryDate: ZonedDateTime,
    val physicalSummaryDate: ZonedDateTime,
    val physicalEventsDate: ZonedDateTime,
    val bodySummaryDate: ZonedDateTime
)
