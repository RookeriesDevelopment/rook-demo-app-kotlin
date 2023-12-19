package com.rookmotion.rookconnectdemo.features.modules.healthconnect.playground

sealed class ClearState {
    object Clearing : ClearState()
    class Error(val message: String) : ClearState()
    object Cleared : ClearState()
}
