package com.rookmotion.rookconnectdemo.features.backgroundsteps

data class BackgroundStepsState(
    val isLoading: Boolean = false,
    val isAvailable: Boolean = false,
    val isActive: Boolean = false,
    val hasPermissions: Boolean = false,
    val steps: Long = 0,
)

