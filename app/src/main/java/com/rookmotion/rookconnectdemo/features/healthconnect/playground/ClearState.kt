package com.rookmotion.rookconnectdemo.features.healthconnect.playground

sealed class ClearState {
    object Clearing : ClearState()
    class Error(val message: String) : ClearState()
    object Cleared : ClearState()
}
