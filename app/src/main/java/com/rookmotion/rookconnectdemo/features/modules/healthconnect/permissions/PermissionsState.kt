package com.rookmotion.rookconnectdemo.features.modules.healthconnect.permissions

sealed class PermissionsState {
    object None : PermissionsState()
    object Loading : PermissionsState()
    class Success(val hasPermissions: Boolean) : PermissionsState()
}