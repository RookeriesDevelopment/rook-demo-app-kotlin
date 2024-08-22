package com.rookmotion.rookconnectdemo.features.yesterdaysync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.domain.enums.HealthConnectAvailability
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import com.rookmotion.rook.sdk.domain.exception.DeviceNotSupportedException
import com.rookmotion.rook.sdk.domain.exception.HealthConnectNotInstalledException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.UserNotInitializedException
import com.rookmotion.rookconnectdemo.data.preferences.RookDemoPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class YesterdaySyncViewModel(
    private val rookPermissionsManager: RookPermissionsManager,
    private val rookDemoPreferences: RookDemoPreferences,
) : ViewModel() {

    private val _hasHealthConnectPermissions = MutableStateFlow(false)
    val hasHealthConnectPermissions get() = _hasHealthConnectPermissions.asStateFlow()

    private val _hasAndroidPermissions = MutableStateFlow(false)
    val hasAndroidPermissions get() = _hasAndroidPermissions.asStateFlow()

    val userAcceptedYesterdaySync: Boolean get() = rookDemoPreferences.getUserAcceptedYesterdaySync()

    init {
        viewModelScope.launch {
            rookPermissionsManager.checkHealthConnectPermissions().fold(
                {
                    _hasHealthConnectPermissions.emit(it)
                },
                {
                    Timber.e("Error checking permissions: $it")
                    _hasHealthConnectPermissions.emit(false)
                }
            )
        }

        viewModelScope.launch {
            val hasAndroidPermissions = rookPermissionsManager.checkAndroidPermissions()

            _hasAndroidPermissions.emit(hasAndroidPermissions)
        }
    }

    fun checkHealthConnectAvailability(): HealthConnectAvailability {
        return rookPermissionsManager.checkHealthConnectAvailability()
    }

    fun openHealthConnect() {
        viewModelScope.launch {
            Timber.i("Opening Health Connect...")

            val result = rookPermissionsManager.openHealthConnectSettings()

            result.fold(
                {
                    Timber.i("Health Connect was opened")
                },
                {
                    val error = when (it) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${it.message}"
                        is UserNotInitializedException -> "UserNotInitializedException: ${it.message}"
                        is HealthConnectNotInstalledException -> "HealthConnectNotInstalledException: ${it.message}"
                        is DeviceNotSupportedException -> "DeviceNotSupportedException: ${it.message}"
                        else -> "${it.message}"
                    }

                    Timber.e("Error opening Health Connect:")
                    Timber.e(error)
                }
            )
        }
    }

    fun requestHealthConnectPermissions() {
        viewModelScope.launch {
            rookPermissionsManager.requestHealthConnectPermissions().fold(
                {
                    when (it) {
                        RequestPermissionsStatus.ALREADY_GRANTED -> {
                            _hasHealthConnectPermissions.emit(true)
                        }

                        RequestPermissionsStatus.REQUEST_SENT -> Unit
                    }
                },
                {
                    Timber.e("Error requesting permissions: $it")
                }
            )
        }
    }

    fun onHealthConnectPermissionsResult(permissionsGranted: Boolean) {
        viewModelScope.launch {
            _hasHealthConnectPermissions.emit(permissionsGranted)
        }
    }

    fun requestAndroidPermissions() {
        viewModelScope.launch {
            val requestPermissionsStatus = rookPermissionsManager.requestAndroidPermissions()

            when (requestPermissionsStatus) {
                RequestPermissionsStatus.ALREADY_GRANTED -> {
                    _hasAndroidPermissions.emit(true)
                }

                RequestPermissionsStatus.REQUEST_SENT -> Unit
            }
        }
    }

    fun onAndroidPermissionsResult(permissionsGranted: Boolean) {
        viewModelScope.launch {
            _hasAndroidPermissions.emit(permissionsGranted)
        }
    }

    fun enableYesterdaySync() {
        viewModelScope.launch {
            rookDemoPreferences.setUserAcceptedYesterdaySync(true)
        }
    }

    fun disableYesterdaySync() {
        viewModelScope.launch {
            rookDemoPreferences.setUserAcceptedYesterdaySync(false)
        }
    }
}