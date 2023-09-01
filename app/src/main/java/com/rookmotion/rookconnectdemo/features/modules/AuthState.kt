package com.rookmotion.rookconnectdemo.features.modules

import com.rookmotion.rook.auth.domain.model.AuthorizationResult

sealed class AuthState {
    object NotAuthorized : AuthState()
    object Loading : AuthState()
    class Error(val message: String) : AuthState()
    class Authorized(val authorizationResult: AuthorizationResult) : AuthState()
}
