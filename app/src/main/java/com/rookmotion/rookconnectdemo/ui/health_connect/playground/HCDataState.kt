package com.rookmotion.rookconnectdemo.ui.health_connect.playground

data class HCDataState<T>(
    val extracting: Boolean = false,
    val extracted: T? = null,
    val extractError: String? = null,
    val enqueueing: Boolean = false,
    val enqueued: Boolean? = null,
    val enqueueError: String? = null,
)
