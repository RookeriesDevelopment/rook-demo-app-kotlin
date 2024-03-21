package com.rookmotion.rookconnectdemo.features.stepstracker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookStepsTracker
import com.rookmotion.rook.sdk.domain.exception.MissingAndroidPermissionsException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

class StepsTrackerViewModel : ViewModel() {

    private val _state = MutableStateFlow(StepsTrackerState())
    val state get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (isActive) {
                RookStepsTracker.getTodaySteps().fold(
                    { todaySteps ->
                        _state.update { it.copy(steps = todaySteps) }
                    },
                    { throwable ->
                        val error = when (throwable) {
                            is SDKNotInitializedException -> "SDKNotInitializedException: ${throwable.message}"
                            else -> "${throwable.message}"
                        }

                        Timber.e("Error obtaining steps: $error")
                    }
                )

                delay(3000)
            }
        }
    }

    fun checkStepsTrackerStatus(context: Context) {
        viewModelScope.launch {
            val isAvailable = RookStepsTracker.isAvailable(context)
            val hasPermissions = RookStepsTracker.hasPermissions(context)
            val isActive = RookStepsTracker.isActive()

            _state.update {
                it.copy(
                    isLoading = false,
                    isAvailable = isAvailable,
                    isActive = isActive,
                    hasPermissions = hasPermissions,
                )
            }
        }
    }

    fun requestStepsTrackerPermissions(context: Context) {
        RookStepsTracker.requestPermissions(context)
    }

    fun startStepsTracker(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            RookStepsTracker.start(context).fold(
                {
                    checkStepsTrackerStatus(context)
                },
                { throwable ->
                    val error = when (throwable) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${throwable.message}"
                        is MissingAndroidPermissionsException -> "MissingAndroidPermissionsException: ${throwable.message}"
                        else -> "${throwable.message}"
                    }

                    Timber.e("Error starting steps tracker: $error")

                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    fun stopStepsTracker(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            RookStepsTracker.stop(context).fold(
                {
                    checkStepsTrackerStatus(context)
                },
                { throwable ->
                    val error = when (throwable) {
                        is SDKNotInitializedException -> "SDKNotInitializedException: ${throwable.message}"
                        else -> "${throwable.message}"
                    }

                    Timber.e("Error stopping steps tracker: $error")

                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }
}