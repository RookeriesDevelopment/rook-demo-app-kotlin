package com.rookmotion.rookconnectdemo.features.sdkconfiguration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.domain.exception.HttpRequestException
import com.rookmotion.rook.sdk.domain.exception.MissingConfigurationException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.TimeoutException
import com.rookmotion.rook.sdk.domain.exception.UserNotInitializedException
import com.rookmotion.rook.sdk.domain.model.RookConfiguration
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.common.isDebug
import com.rookmotion.rookconnectdemo.common.rookEnvironment
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
        val rookConfiguration = RookConfiguration(
            BuildConfig.CLIENT_UUID,
            BuildConfig.SECRET_KEY,
            rookEnvironment,
        )

        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Using configuration:")
            stringBuilder.appendConsoleLine("$rookConfiguration")
            _configuration.emit(stringBuilder.toString())

            if (isDebug) {
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
                    checkUserIDRegistered()
                    _initialize.emit(stringBuilder.toString())
                },
                {
                    val error = when (it) {
                        is MissingConfigurationException -> "MissingConfigurationException: ${it.message}"
                        is TimeoutException -> "TimeoutException: ${it.message}"
                        else -> "${it.message}"
                    }

                    stringBuilder.appendConsoleLine("Error initializing SDK:")
                    stringBuilder.appendConsoleLine(error)
                    _initialize.emit(stringBuilder.toString())
                }
            )
        }
    }

    private fun checkUserIDRegistered() {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            val userID = rookConfigurationManager.getUserID()

            if (userID != null) {
                stringBuilder.appendConsoleLine("Found local userID $userID, you can skip step 3")
                _enableNavigation.emit(true)
            } else {
                stringBuilder.appendConsoleLine("Local userID not found, please set a userID")
            }

            _user.emit(stringBuilder.toString())
        }
    }

    fun updateUserID(userID: String) {
        val stringBuilder = StringBuilder()

        viewModelScope.launch {
            stringBuilder.appendConsoleLine("Updating userID...")
            _user.emit(stringBuilder.toString())

            rookConfigurationManager.updateUserID(userID).fold(
                {
                    stringBuilder.appendConsoleLine("userID updated successfully")
                    _user.emit(stringBuilder.toString())
                    _enableNavigation.emit(true)
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is TimeoutException -> "TimeoutException: ${it.message}"
                        else -> "${it.message}"
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
                        else -> "${it.message}"
                    }

                    Timber.e("Error updating user timezone:")
                    Timber.e(error)
                }
            )
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            Timber.i("Deleting user from rook...")

            val result = rookConfigurationManager.deleteUserFromRook()

            result.fold(
                {
                    Timber.i("User delete from rook")
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                        else -> "${it.message}"
                    }

                    Timber.e("Error deleting user from rook:")
                    Timber.e(error)
                }
            )
        }
    }
}
