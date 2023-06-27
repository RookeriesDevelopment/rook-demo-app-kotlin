package com.rookmotion.rookconnectdemo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookmotion.rookconnectdemo.features.healthconnect.permissions.HCPermissionsViewModel
import com.rookmotion.rookconnectdemo.features.healthconnect.playground.HCPlaygroundViewModel
import com.rookmotion.rookconnectdemo.features.selector.AuthViewModel
import com.rookmotion.rookconnectdemo.features.selector.UserViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(serviceLocator.authorizationProvider) as T
        }

        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(serviceLocator.rookUsersManager) as T
        }

        if (modelClass.isAssignableFrom(HCPermissionsViewModel::class.java)) {
            return HCPermissionsViewModel(
                rookHealthConnectManager = serviceLocator.rookHealthConnectManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(HCPlaygroundViewModel::class.java)) {
            return HCPlaygroundViewModel(
                rookTransmissionManager = serviceLocator.rookTransmissionManager,
                rookHealthConnectManager = serviceLocator.rookHealthConnectManager,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}