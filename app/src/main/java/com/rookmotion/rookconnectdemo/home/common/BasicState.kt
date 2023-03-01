package com.rookmotion.rookconnectdemo.home.common

sealed class BasicState {
    object None : BasicState()
    object Loading : BasicState()
    class Error(val message: String) : BasicState()
    object Success : BasicState()
}
