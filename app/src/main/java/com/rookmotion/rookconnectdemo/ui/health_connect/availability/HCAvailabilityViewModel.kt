package com.rookmotion.rookconnectdemo.ui.health_connect.availability

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rookmotion.rook.health_connect.RookHealthConnectAvailability
import com.rookmotion.rook.health_connect.domain.enums.HCAvailabilityStatus
import com.rookmotion.rookconnectdemo.ui.common.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HCAvailabilityViewModel : ViewModel() {

    private val _isAvailable = MutableStateFlow<DataState<HCAvailabilityStatus>>(DataState.None)
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