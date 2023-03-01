package com.rookmotion.rookconnectdemo.home.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookmotion.rookconnectdemo.di.ServiceLocator
import com.rookmotion.rookconnectdemo.home.health_connect.HealthConnectViewModel
import com.rookmotion.rookconnectdemo.home.selector.AuthViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                provider = serviceLocator.authorizationProvider,
                manager = serviceLocator.rookUsersManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(HealthConnectViewModel::class.java)) {
            return HealthConnectViewModel(
                transmission = serviceLocator.rookTransmissionManager,
                healthConnect = serviceLocator.rookHealthConnectManager,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}