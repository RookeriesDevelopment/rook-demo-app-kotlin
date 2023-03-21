package com.rookmotion.rookconnectdemo.utils

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object RookDateTimeUtils {
    private val utc: ZoneId get() = ZoneId.of("UTC")
    private val rookDateTimeZFormatter: DateTimeFormatter get() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private val rookDateTimeFormatter: DateTimeFormatter get() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    private val rookDateFormatter: DateTimeFormatter get() = DateTimeFormatter.ISO_LOCAL_DATE

    fun toUTC(date: ZonedDateTime): ZonedDateTime {
        val dateUTC = if (date.offset == ZoneOffset.UTC) {
            date
        } else {
            date.withZoneSameInstant(utc)
        }

        val rookDateString = rookDateTimeFormatter.format(dateUTC).plus("Z")

        return stringToZonedDateTime(rookDateString)
    }

    fun stringToZonedDateTime(string: String): ZonedDateTime {
        val date = ZonedDateTime.parse(string, rookDateTimeZFormatter)

        return if (date.offset == ZoneOffset.UTC) {
            date
        } else {
            date.withZoneSameInstant(utc)
        }
    }

    fun stringToLocalDate(string: String): LocalDate {
        return LocalDate.parse(string, rookDateFormatter)
    }
}