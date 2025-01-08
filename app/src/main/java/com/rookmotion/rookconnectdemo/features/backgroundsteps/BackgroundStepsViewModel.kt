package com.rookmotion.rookconnectdemo.features.backgroundsteps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.RookStepsManager
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
                    isStepsServiceAvailable = isAvailable,
                    isTrackingSteps = isActive,
                    hasAndroidPermissions = hasAndroidPermissions,
                )
            }
        }
    }

    fun requestAndroidPermissions() {
        rookPermissionsManager.requestAndroidPermissions()
    }

    fun enableBackgroundSteps() {
        Timber.i("Enabling background steps")

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            rookStepsManager.enableBackgroundAndroidSteps().fold(
                {
                    checkStepsServiceStatus()
                    Timber.i("Background steps enabled")
                },
                { throwable ->
                    _state.update { it.copy(isLoading = false) }
                    Timber.e("Error enabling background steps: ${throwable.message}")
                }
            )
        }
    }

    fun disableBackgroundSteps() {
        Timber.i("Disabling background steps")

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            rookStepsManager.disableBackgroundAndroidSteps().fold(
                {
                    checkStepsServiceStatus()
                    Timber.i("Background steps disabled")
                },
                { throwable ->
                    _state.update { it.copy(isLoading = false) }
                    Timber.e("Error disabling background steps: ${throwable.message}")
                }
            )
        }
    }

    fun syncTodaySteps() {
        Timber.i("Syncing today steps")

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            rookStepsManager.syncTodayAndroidStepsCount().fold(
                { todaySteps ->
                    _state.update {
                        it.copy(isLoading = false, steps = todaySteps)
                    }
                    Timber.i("Today steps synced: $todaySteps")
                },
                { throwable ->
                    _state.update { it.copy(isLoading = false) }
                    Timber.e("Error syncing today steps: ${throwable.message}")
                }
            )
        }
    }
}