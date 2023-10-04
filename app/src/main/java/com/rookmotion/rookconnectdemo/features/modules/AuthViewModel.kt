package com.rookmotion.rookconnectdemo.features.modules

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.health_connect.RookHealthConnectAuthorization
import com.rookmotion.rook.transmission.RookTransmissionAuthorization
import com.rookmotion.rook.users.RookUsersAuthorization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _transmissionAuthorization = MutableStateFlow<AuthorizationState>(
        AuthorizationState.None
    )
    val transmissionAuthorization get() = _transmissionAuthorization.asStateFlow()

    private val _healthConnectAuthorization = MutableStateFlow<AuthorizationState>(
        AuthorizationState.None
    )
    val healthConnectAuthorization get() = _healthConnectAuthorization.asStateFlow()

    private val _usersAuthorization = MutableStateFlow<AuthorizationState>(
        AuthorizationState.None
    )
    val usersAuthorization get() = _usersAuthorization.asStateFlow()

    fun authorizeTransmission(context: Context, clientUUID: String) {
        if (
            transmissionAuthorization.value == AuthorizationState.Loading ||
            transmissionAuthorization.value is AuthorizationState.Authorized
        ) {
            return
        }

        viewModelScope.launch {
            _transmissionAuthorization.emit(AuthorizationState.Loading)

            val result = RookTransmissionAuthorization.authorize(context, clientUUID)

            result.fold(
                {
                    _transmissionAuthorization.emit(AuthorizationState.Authorized(it))
                },
                {
                    val error = "AuthorizationError: ${it.localizedMessage}"

                    _transmissionAuthorization.emit(AuthorizationState.NotAuthorized(error))
                }
            )
        }
    }

    fun authorizeHealthConnect(context: Context, clientUUID: String) {
        if (
            healthConnectAuthorization.value == AuthorizationState.Loading ||
            healthConnectAuthorization.value is AuthorizationState.Authorized
        ) {
            return
        }

        viewModelScope.launch {
            _healthConnectAuthorization.emit(AuthorizationState.Loading)

            val result = RookHealthConnectAuthorization.authorize(context, clientUUID)

            result.fold(
                {
                    _healthConnectAuthorization.emit(AuthorizationState.Authorized(it))
                },
                {
                    val error = "AuthorizationError: ${it.localizedMessage}"

                    _healthConnectAuthorization.emit(AuthorizationState.NotAuthorized(error))
                }
            )
        }
    }

    fun authorizeUsers(context: Context, clientUUID: String) {
        if (
            usersAuthorization.value == AuthorizationState.Loading ||
            usersAuthorization.value is AuthorizationState.Authorized
        ) {
            return
        }

        viewModelScope.launch {
            _usersAuthorization.emit(AuthorizationState.Loading)

            val result = RookUsersAuthorization.authorize(context, clientUUID)

            result.fold(
                {
                    _usersAuthorization.emit(AuthorizationState.Authorized(it))
                },
                {
                    val error = "AuthorizationError: ${it.localizedMessage}"

                    _usersAuthorization.emit(AuthorizationState.NotAuthorized(error))
                }
            )
        }
    }
}
