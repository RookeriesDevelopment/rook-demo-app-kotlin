package com.rookmotion.rookconnectdemo.home.common

sealed class DataState<out T> {
    object None : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    class Error(val message: String) : DataState<Nothing>()
    class Success<T>(val data: T) : DataState<T>()
}