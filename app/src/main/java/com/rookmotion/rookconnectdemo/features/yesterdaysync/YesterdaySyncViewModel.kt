package com.rookmotion.rookconnectdemo.features.yesterdaysync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.domain.exception.DeviceNotSupportedException
import com.rookmotion.rook.sdk.domain.exception.HealthConnectNotInstalledException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.UserNotInitializedException
import com.rookmotion.rookconnectdemo.data.preferences.RookDemoPreferences
import kotlinx.coroutines.launch
import timber.log.Timber

class YesterdaySyncViewModel(
    private val rookHealthPermissionsManager: RookHealthPermissionsManager,
    private val rookDemoPreferences: RookDemoPreferences,
) : ViewModel() {

    val userAcceptedYesterdaySync: Boolean get() = rookDemoPreferences.getUserAcceptedYesterdaySync()

    fun openHealthConnect() {
        viewModelScope.launch {
            Timber.i("Opening Health Connect...")

            val result = rookHealthPermissionsManager.openHealthConnectSettings()

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