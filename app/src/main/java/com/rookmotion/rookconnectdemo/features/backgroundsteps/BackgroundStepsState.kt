package com.rookmotion.rookconnectdemo.features.backgroundsteps

data class BackgroundStepsState(
    val isLoading: Boolean = false,
    val isStepsServiceAvailable: Boolean = false,
    val hasAndroidPermissions: Boolean = false,
    val isTrackingSteps: Boolean = false,
    val steps: Long = 0,
)

