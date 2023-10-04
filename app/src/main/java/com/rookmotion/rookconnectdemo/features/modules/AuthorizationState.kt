package com.rookmotion.rookconnectdemo.features.modules

import java.time.Instant

sealed class AuthorizationState {
    object None : AuthorizationState()
    object Loading : AuthorizationState()
    class NotAuthorized(val message: String) : AuthorizationState()
    class Authorized(val expirationDate: Instant) : AuthorizationState()
}
