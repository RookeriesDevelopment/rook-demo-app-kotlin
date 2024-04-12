package com.rookmotion.rookconnectdemo.features.backgroundsteps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.sdk.RookStepsManager
import com.rookmotion.rook.sdk.domain.exception.MissingAndroidPermissionsException
import com.rookmotion.rook.sdk.domain.exception.SDKNotInitializedException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

class BackgroundStepsViewModel(private val rookStepsManager: RookStepsManager) : ViewModel() {

    private val _state = MutableStateFlow(BackgroundStepsState())
    val state get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (isActive) {
                rookStepsManager.getTodaySteps().fold(
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

    fun observeTodaySteps(): Flow<Long> {
        return rookStepsManager.observeStepsOf(LocalDate.now())
    }

    fun checkStepsServiceStatus() {
        viewModelScope.launch {
            val isAvailable = rookStepsManager.isAvailable()
            val hasPermissions = rookStepsManager.hasPermissions()
            val isActive = rookStepsManager.isActive()

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

    fun requestStepsPermissions() {
        rookStepsManager.requestPermissions()
    }

    fun startStepsService() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            rookStepsManager.start().fold(
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

            rookStepsManager.stop().fold(
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
}