package com.rookmotion.rookconnectdemo.features.sdkconfiguration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.domain.environment.RookEnvironment
import com.rookmotion.rook.sdk.domain.exception.HttpRequestException
import com.rookmotion.rook.sdk.domain.exception.MissingConfigurationException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.TimeoutException
import com.rookmotion.rook.sdk.domain.exception.UserNotInitializedException
import com.rookmotion.rook.sdk.domain.model.RookConfiguration
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.extension.appendConsoleLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SDKConfigurationViewModel(
    private val rookConfigurationManager: RookConfigurationManager,
) : ViewModel() {

    private val _configuration = MutableStateFlow("")
    val configuration get() = _configuration.asStateFlow()

    private val _initialize = MutableStateFlow("")
    val initialize get() = _initialize.asStateFlow()

    private val _user = MutableStateFlow("")
    val user get() = _user.asStateFlow()

    private val _enableNavigation = MutableStateFlow(false)
    val enableNavigation get() = _enableNavigation.asStateFlow()

    fun setConfiguration() {
        val environment = if (BuildConfig.DEBUG) RookEnvironment.SANDBOX
        else RookEnvironment.PRODUCTION

        val rookConfiguration = RookConfiguration(
            BuildConfig.CLIENT_UUID,
            BuildConfig.SECRET_KEY,
            environment,
        )

        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Using configuration:")
            stringBuilder.appendConsoleLine("$rookConfiguration")
            _configuration.emit(stringBuilder.toString())

            if (BuildConfig.DEBUG) {
                rookConfigurationManager.enableLocalLogs()
            }

            rookConfigurationManager.setConfiguration(rookConfiguration)

            stringBuilder.appendConsoleLine("Configuration set successfully")
            _configuration.emit(stringBuilder.toString())
        }
    }

    fun initialize() {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Initializing...")
            _initialize.emit(stringBuilder.toString())

            val result = rookConfigurationManager.initRook()

            result.fold(
                {
                    stringBuilder.appendConsoleLine("SDK initialized successfully")
                    _initialize.emit(stringBuilder.toString())
                },
                {
                    val error = when (it) {
                        is MissingConfigurationException -> "MissingConfigurationException: ${it.message}"
                        is TimeoutException -> "TimeoutException: ${it.message}"
                        else -> it.localizedMessage
                    }

                    stringBuilder.appendConsoleLine("Error initializing SDK:")
                    stringBuilder.appendConsoleLine(error)
                    _initialize.emit(stringBuilder.toString())
                }
            )
        }
    }

    fun updateUserID(userID: String) {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Updating userID...")
            _user.emit(stringBuilder.toString())

            val result = rookConfigurationManager.updateUserID(userID)

            result.fold(
                {
                    stringBuilder.appendConsoleLine("userID updated successfully")
                    _user.emit(stringBuilder.toString())
                    _enableNavigation.emit(true)
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is TimeoutException -> "TimeoutException: ${it.message}"
                        else -> it.localizedMessage
                    }

                    stringBuilder.appendConsoleLine("Error updating userID:")
                    stringBuilder.appendConsoleLine(error)
                    _user.emit(stringBuilder.toString())
                }
            )
        }
    }

    fun updateTimeZoneInformation() {
        viewModelScope.launch {
            Timber.i("Updating user timezone...")

            val result = rookConfigurationManager.syncUserTimeZone()

            result.fold(
                {
                    Timber.i("User timezone updated successfully")
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                        is TimeoutException -> "TimeoutException: ${it.message}"
                        is HttpRequestException -> "HttpRequestException: ${it.message}"
                        else -> it.localizedMessage
                    }

                    Timber.e("Error updating user timezone:")
                    Timber.e(error)
                }
            )
        }
    }
}
