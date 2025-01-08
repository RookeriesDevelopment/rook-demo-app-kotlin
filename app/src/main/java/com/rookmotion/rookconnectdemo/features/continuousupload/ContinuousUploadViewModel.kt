package com.rookmotion.rookconnectdemo.features.continuousupload

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookContinuousUploadManager
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import com.rookmotion.rookconnectdemo.common.ConsoleOutput
import com.rookmotion.rookconnectdemo.common.isDebug
import com.rookmotion.rookconnectdemo.data.preferences.RookDemoPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ContinuousUploadViewModel(
    private val rookPermissionsManager: RookPermissionsManager,
    private val rookContinuousUploadManager: RookContinuousUploadManager,
    private val rookDemoPreferences: RookDemoPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow(ContinuousUploadState())
    val state get() = _state.asStateFlow()

    val continuousUploadOutput = ConsoleOutput()

    fun onRefresh() {
        viewModelScope.launch {
            checkPermissions()
            automaticallyStartContinuousUpload()
        }
    }

    fun requestAndroidPermissions() {
        viewModelScope.launch {
            val requestPermissionsStatus = rookPermissionsManager.requestAndroidPermissions()

            when (requestPermissionsStatus) {
                RequestPermissionsStatus.ALREADY_GRANTED -> _state.update {
                    it.copy(hasAndroidPermissions = true)
                }

                RequestPermissionsStatus.REQUEST_SENT -> Timber.i("Android permissions request sent")
            }
        }
    }

    fun onAndroidPermissionsResult(permissionsGranted: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(hasAndroidPermissions = permissionsGranted)
            }
        }
    }

    fun requestHealthConnectPermissions() {
        viewModelScope.launch {
            rookPermissionsManager.requestHealthConnectPermissions().fold(
                { requestPermissionsStatus ->
                    when (requestPermissionsStatus) {
                        RequestPermissionsStatus.ALREADY_GRANTED -> _state.update {
                            it.copy(hasHealthConnectPermissions = true)
                        }

                        RequestPermissionsStatus.REQUEST_SENT -> Timber.i("Health Connect permissions request sent")
                    }
                },
                {
                    Timber.e(it, "Error requesting Health Connect permissions")
                },
            )
        }
    }

    fun onHealthConnectPermissionsResult(permissionsGranted: Boolean, permissionsPartiallyGranted: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(hasHealthConnectPermissions = permissionsGranted || permissionsPartiallyGranted)
            }
        }
    }

    fun openHealthConnectSettings() {
        viewModelScope.launch {
            rookPermissionsManager.openHealthConnectSettings().fold(
                {
                    Timber.i("Health Connect settings opened")
                },
                {
                    Timber.e(it, "Error opening Health Connect settings")
                }
            )
        }
    }

    fun enableContinuousUpload() {
        viewModelScope.launch {
            rookDemoPreferences.setUserAcceptedContinuousUpload(true)
            automaticallyStartContinuousUpload()
        }
    }

    fun disableContinuousUpload() {
        viewModelScope.launch {
            rookDemoPreferences.setUserAcceptedContinuousUpload(false)
            automaticallyStartContinuousUpload()
        }
    }

    private suspend fun checkPermissions() {
        val androidPermissions = rookPermissionsManager.checkAndroidPermissions()
        val hcPermissions = rookPermissionsManager.checkHealthConnectPermissions().getOrDefault(
            defaultValue = false
        )
        val hcPartialPermissions = rookPermissionsManager.checkHealthConnectPermissionsPartially().getOrDefault(
            defaultValue = false
        )

        _state.update {
            it.copy(
                hasAndroidPermissions = androidPermissions,
                hasHealthConnectPermissions = hcPermissions || hcPartialPermissions,
            )
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun automaticallyStartContinuousUpload() {
        continuousUploadOutput.set("Verifying if user accepted continuous upload...")

        val userAcceptedContinuousUpload = rookDemoPreferences.getUserAcceptedContinuousUpload()

        _state.update { it.copy(isContinuousUploadEnabled = userAcceptedContinuousUpload) }

        if (!userAcceptedContinuousUpload) {
            continuousUploadOutput.append("User did not accept continuous upload")
            return
        }

        continuousUploadOutput.append("Verifying if permissions are granted...")

        val hasPermissions = _state.value.hasAndroidPermissions && _state.value.hasHealthConnectPermissions

        if (!hasPermissions) {
            continuousUploadOutput.append("Permissions not granted")
            return
        }

        continuousUploadOutput.append("Starting continuous upload...")

        rookContinuousUploadManager.launchInForegroundService(isDebug).fold(
            {
                continuousUploadOutput.append("Continuous upload started")
            },
            {
                continuousUploadOutput.appendError(it, "Error starting continuous upload")
            }
        )
    }
}