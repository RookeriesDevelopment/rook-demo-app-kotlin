package com.rookmotion.rookconnectdemo.features.modules.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.health_connect.RookHealthConnectConfiguration
import com.rookmotion.rook.transmission.RookTransmissionConfiguration
import com.rookmotion.rook.users.RookUsersConfiguration
import com.rookmotion.rookconnectdemo.common.isDebug
import com.rookmotion.rookconnectdemo.common.rookHealthConnectEnvironment
import com.rookmotion.rookconnectdemo.common.rookTransmissionEnvironment
import com.rookmotion.rookconnectdemo.common.rookUsersEnvironment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _transmissionInitialization = MutableStateFlow<InitializationState>(
        InitializationState.None
    )
    val transmissionInitialization get() = _transmissionInitialization.asStateFlow()

    private val _healthConnectInitialization = MutableStateFlow<InitializationState>(
        InitializationState.None
    )
    val healthConnectInitialization get() = _healthConnectInitialization.asStateFlow()

    private val _usersInitialization = MutableStateFlow<InitializationState>(
        InitializationState.None
    )
    val usersInitialization get() = _usersInitialization.asStateFlow()

    fun initializeTransmission(context: Context, clientUUID: String) {
        if (
            transmissionInitialization.value == InitializationState.Loading ||
            transmissionInitialization.value is InitializationState.Success
        ) {
            return
        }

        viewModelScope.launch {
            _transmissionInitialization.emit(InitializationState.Loading)

            val result = RookTransmissionConfiguration.initRookTransmission(
                context = context,
                clientUUID = clientUUID,
                environment = rookTransmissionEnvironment,
                enableLogs = isDebug,
            )

            result.fold(
                {
                    _transmissionInitialization.emit(InitializationState.Success(it))
                },
                {
                    val error = "RookTransmissionConfiguration: ${it.localizedMessage}"

                    _transmissionInitialization.emit(InitializationState.Error(error))
                }
            )
        }
    }

    fun initializeHealthConnect(
        context: Context,
        userID: String,
        clientUUID: String,
        secretKey: String,
    ) {
        if (
            healthConnectInitialization.value == InitializationState.Loading ||
            healthConnectInitialization.value is InitializationState.Success
        ) {
            return
        }

        viewModelScope.launch {
            _healthConnectInitialization.emit(InitializationState.Loading)

            val result = RookHealthConnectConfiguration.initRookHealthConnect(
                context = context,
                clientUUID = clientUUID,
                secretKey = secretKey,
                environment = rookHealthConnectEnvironment,
                enableLogs = isDebug,
            )

            result.fold(
                {
                    // Needed to enable data source updates and to configure the owner of HC summaries and events
                    RookHealthConnectConfiguration.setUserID(userID)

                    _healthConnectInitialization.emit(InitializationState.Success(it))
                },
                {
                    val error = "RookHealthConnectConfiguration: ${it.localizedMessage}"

                    _healthConnectInitialization.emit(InitializationState.Error(error))
                }
            )
        }
    }

    fun initializeUsers(context: Context, clientUUID: String) {
        if (
            usersInitialization.value == InitializationState.Loading ||
            usersInitialization.value is InitializationState.Success
        ) {
            return
        }

        viewModelScope.launch {
            _usersInitialization.emit(InitializationState.Loading)

            val result = RookUsersConfiguration.initRookUsers(
                context = context,
                clientUUID = clientUUID,
                environment = rookUsersEnvironment,
                enableLogs = isDebug,
            )

            result.fold(
                {
                    _usersInitialization.emit(InitializationState.Success(it))
                },
                {
                    val error = "RookUsersConfiguration: ${it.localizedMessage}"

                    _usersInitialization.emit(InitializationState.Error(error))
                }
            )
        }
    }
}