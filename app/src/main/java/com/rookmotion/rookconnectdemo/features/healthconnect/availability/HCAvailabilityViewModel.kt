package com.rookmotion.rookconnectdemo.features.healthconnect.availability

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.health_connect.RookHealthConnectAvailability
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HCAvailabilityViewModel : ViewModel() {

    private val _isAvailable = MutableStateFlow<AvailabilityState>(AvailabilityState.None)
    val isAvailable get() = _isAvailable.asStateFlow()

    fun checkAvailability(context: Context) {
        viewModelScope.launch {
            _isAvailable.emit(AvailabilityState.Loading)

            try {
                val result = RookHealthConnectAvailability.checkAvailability(context)

                _isAvailable.emit(AvailabilityState.Success(result))
            } catch (e: Exception) {
                _isAvailable.emit(AvailabilityState.Error(e.toString()))
            }
        }
    }
}
