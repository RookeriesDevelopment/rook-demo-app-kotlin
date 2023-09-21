package com.rookmotion.rookconnectdemo.features.healthconnect.playground

import com.rookmotion.rook.health_connect.domain.time.UserTimeZone

data class UserTimeZoneState(
    val extracting: Boolean = false,
    val extracted: UserTimeZone? = null,
    val extractError: String? = null,
    val uploading: Boolean = false,
    val uploaded: Boolean = false,
    val uploadError: String? = null,
)
