package com.rookmotion.rookconnectdemo.extension

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun StringBuilder.appendConsoleLine(string: String): StringBuilder {
    val timestamp = "${formatter.format(ZonedDateTime.now())} - "

    return append(timestamp).append(string).append("\n").append("\n")
}

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
