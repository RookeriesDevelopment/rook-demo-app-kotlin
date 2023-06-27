package com.rookmotion.rookconnectdemo.features.healthconnect.permissions

sealed class PermissionsState {
    object None : PermissionsState()
    object Loading : PermissionsState()
    class Success(val hasPermissions: Boolean) : PermissionsState()
}