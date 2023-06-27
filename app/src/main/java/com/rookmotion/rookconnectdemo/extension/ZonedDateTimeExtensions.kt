package com.rookmotion.rookconnectdemo.extension

import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun ZonedDateTime.toUTCSameInstant(): ZonedDateTime {
    return if (offset == ZoneOffset.UTC) {
        this
    } else {
        withZoneSameInstant(ZoneId.of("UTC"))
    }
}
