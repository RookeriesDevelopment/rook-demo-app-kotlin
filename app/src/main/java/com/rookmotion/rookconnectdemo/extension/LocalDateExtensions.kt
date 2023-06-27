package com.rookmotion.rookconnectdemo.extension

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun LocalDate.atStartOfDayUTC(): ZonedDateTime {
    return atStartOfDay(ZoneId.of("UTC"))
}