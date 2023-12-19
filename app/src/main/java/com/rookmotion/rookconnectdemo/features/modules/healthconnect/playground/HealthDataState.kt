package com.rookmotion.rookconnectdemo.features.modules.healthconnect.playground

data class HealthDataState<T>(
    val extracting: Boolean = false,
    val extracted: T? = null,
    val extractError: String? = null,
    val enqueueing: Boolean = false,
    val enqueued: Boolean = false,
    val enqueueError: String? = null,
)
