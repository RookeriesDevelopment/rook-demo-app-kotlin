package com.rookmotion.rookconnectdemo.features.modules.healthconnect.availability

import com.rookmotion.rook.health_connect.domain.enums.HCAvailabilityStatus

sealed class AvailabilityState {
    object None : AvailabilityState()
    object Loading : AvailabilityState()
    class Error(val message: String) : AvailabilityState()
    class Success(val availabilityStatus: HCAvailabilityStatus) : AvailabilityState()
}
