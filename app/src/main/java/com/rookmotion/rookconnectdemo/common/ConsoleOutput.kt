package com.rookmotion.rookconnectdemo.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ConsoleOutput {
    private val content = StringBuilder()

    private val _output = MutableStateFlow(value = "")
    val output get() = _output.asStateFlow()

    private fun buildTimestamp(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

        return "${formatter.format(ZonedDateTime.now())} - "
    }

    private fun notifyFlow() {
        _output.value = content.toString()
    }

    /**
     * Clears the content of this output and sets a new string.
     */
    fun set(string: String) {
        Timber.i(string)
        content.clear()
        content.append(buildTimestamp()).append(string).append("\n").append("\n")
        notifyFlow()
    }

    /**
     * Appends a string to the output.
     */
    fun append(string: String) {
        Timber.i(string)
        content.append(buildTimestamp()).append(string).append("\n").append("\n")
        notifyFlow()
    }

    /**
     * Appends multiple strings to the output separated by new lines.
     */
    fun appendMultiple(vararg strings: String) {
        content.append(buildTimestamp())

        for (string in strings) {
            Timber.i(string)
            content.append(string).append("\n").append("\n")
        }

        notifyFlow()
    }

    /**
     * Appends an error and an optional string to the output.
     */
    fun appendError(throwable: Throwable, string: String? = null) {
        Timber.e(throwable, string)
        content.append(buildTimestamp())

        if (string != null) {
            content.append(string).append(": ").append("\n")
        }

        content.append(throwable).append("\n").append("\n")

        notifyFlow()
    }

    /**
     * Clears the content of this output making it empty.
     */
    fun clear() {
        content.clear()
        notifyFlow()
    }

    override fun toString(): String {
        return content.toString()
    }
}
