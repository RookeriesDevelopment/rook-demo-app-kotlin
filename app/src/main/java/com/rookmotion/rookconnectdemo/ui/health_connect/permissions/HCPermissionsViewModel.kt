package com.rookmotion.rookconnectdemo.ui.health_connect.permissions

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookmotion.rook.health_connect.RookHealthConnectManager
import com.rookmotion.rookconnectdemo.ui.common.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HCPermissionsViewModel(
    private val healthConnect: RookHealthConnectManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _hasPermissions = MutableStateFlow<DataState<Boolean>>(DataState.None)
    val hasPermissions get() = _hasPermissions.asStateFlow()

    fun openHealthConnectSettings() {
        healthConnect.openHealthConnectSettings()
    }

    fun checkPermissions() {
        _hasPermissions.tryEmit(DataState.Loading)

        viewModelScope.launch(dispatcher) {
            try {
                val result = healthConnect.hasAllPermissions()

                _hasPermissions.emit(DataState.Success(result))
            } catch (e: Exception) {
                _hasPermissions.emit(DataState.Error(e.toString()))
            }
        }
    }

    fun requestPermissions(activity: Activity) {
        healthConnect.requestAllPermissions(activity)
    }
}