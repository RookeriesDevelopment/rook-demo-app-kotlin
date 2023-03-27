package com.rookmotion.rookconnectdemo.home.health_connect

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rookmotion.rook.health_connect.RookHealthConnectAvailability
import com.rookmotion.rook.health_connect.domain.enums.AvailabilityStatus
import com.rookmotion.rookconnectdemo.home.common.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HealthConnectAvailabilityViewModel: ViewModel() {

    private val _isAvailable = MutableStateFlow<DataState<AvailabilityStatus>>(DataState.None)
    val isAvailable get() = _isAvailable.asStateFlow()

    fun checkAvailability(context: Context) {
        _isAvailable.tryEmit(DataState.Loading)

        try {
            val result = RookHealthConnectAvailability.checkAvailability(context)

            _isAvailable.tryEmit(DataState.Success(result))
        } catch (e: Exception) {
            _isAvailable.tryEmit(DataState.Error(e.toString()))
        }
    }
}