package com.rookmotion.rookconnectdemo.features.modules.menu

import java.time.Instant

sealed class InitializationState {
    object None : InitializationState()
    object Loading : InitializationState()
    class Error(val message: String) : InitializationState()
    class Success(val expirationDate: Instant) : InitializationState()
}
