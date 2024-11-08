package com.rookmotion.rookconnectdemo.features.backgroundsteps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.RookStepsManager
import com.rookmotion.rook.sdk.domain.exception.MissingAndroidPermissionsException
import com.rookmotion.rook.sdk.domain.exception.SDKNotAuthorizedException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class BackgroundStepsViewModel(
    private val rookPermissionsManager: RookPermissionsManager,
    private val rookStepsManager: RookStepsManager
) : ViewModel() {

    private val _state = MutableStateFlow(BackgroundStepsState())
    val state get() = _state.asStateFlow()

    fun checkStepsServiceStatus() {
        viewModelScope.launch {
            val isAvailable = rookStepsManager.isAvailable()
            val hasAndroidPermissions = rookPermissionsManager.checkAndroidPermissions()
            val isActive = rookStepsManager.isBackgroundAndroidStepsActive()

            _state.update {
                it.copy(
                    isLoading = false,
                    isAvailable = isAvailable,
                    isActive = isActive,
                    hasAndroidPermissions = hasAndroidPermissions,
                )
            }
        }
    }

    fun requestAndroidPermissions() {
        rookPermissionsManager.requestAndroidPermissions()
    }

    fun startStepsService() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            rookStepsManager.enableBackgroundAndroidSteps().fold(
                {
                    checkStepsServiceStatus()
                },
                { throwable ->
                    val error = when (throwable) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${throwable.message}"
                        is MissingAndroidPermissionsException -> "MissingAndroidPermissionsException: ${throwable.message}"
                        else -> "${throwable.message}"
                    }

                    Timber.e("Error starting steps : $error")

                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    fun stopStepsService() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            rookStepsManager.disableBackgroundAndroidSteps().fold(
                {
                    checkStepsServiceStatus()
                },
                { throwable ->
                    val error = when (throwable) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${throwable.message}"
                        else -> "${throwable.message}"
                    }

                    Timber.e("Error stopping steps service: $error")

                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    fun syncTodaySteps() {
        viewModelScope.launch {
            rookStepsManager.syncTodayAndroidStepsCount().fold(
                { todaySteps ->
                    _state.update { it.copy(steps = todaySteps) }
                },
                { throwable ->
                    val error = when (throwable) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${throwable.message}"
                        is SDKNotAuthorizedException -> "SDKNotAuthorizedException: ${throwable.message}"
                        else -> "${throwable.message}"
                    }

                    Timber.e("Error obtaining steps: $error")
                }
            )
        }
    }
}