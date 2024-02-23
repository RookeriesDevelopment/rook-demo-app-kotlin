package com.rookmotion.rookconnectdemo.features.monolithic.yesterdaysyncpermissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.domain.exception.DeviceNotSupportedException
import com.rookmotion.rook.sdk.domain.exception.HealthConnectNotInstalledException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import com.rookmotion.rook.sdk.domain.exception.UserNotInitializedException
import kotlinx.coroutines.launch
import timber.log.Timber

class YesterdaySyncPermissionsViewModel(
    private val rookHealthPermissionsManager: RookHealthPermissionsManager,
) : ViewModel() {

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
}