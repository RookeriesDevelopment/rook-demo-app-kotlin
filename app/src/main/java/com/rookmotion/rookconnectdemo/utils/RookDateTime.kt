package com.rookmotion.rookconnectdemo.utils

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val utc: ZoneId get() = ZoneId.of("UTC")
private val rookDateTimeZFormatter: DateTimeFormatter get() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz")
private val rookDateFormatter: DateTimeFormatter get() = DateTimeFormatter.ISO_LOCAL_DATE

fun toUTCSameInstant(date: ZonedDateTime): ZonedDateTime {
    val dateUTC = if (date.offset == ZoneOffset.UTC) {
        date
    } else {
        date.withZoneSameInstant(utc)
    }

    return dateUTC
}

fun toUTCZonedDateTime(string: String): ZonedDateTime {
    val date = ZonedDateTime.parse(string, rookDateTimeZFormatter)

    return date.withZoneSameInstant(utc)
}

fun toLocalDate(string: String): LocalDate {
    return LocalDate.parse(string, rookDateFormatter)
}