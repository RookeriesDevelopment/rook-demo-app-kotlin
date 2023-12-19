package com.rookmotion.rookconnectdemo.features.modules.menu

sealed class UserState {
    object None : UserState()
    object Loading : UserState()
    class Error(val message: String) : UserState()
    object Registered : UserState()
}
