package com.rookmotion.rookconnectdemo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookmotion.rookconnectdemo.ui.health_connect.permissions.HCPermissionsViewModel
import com.rookmotion.rookconnectdemo.ui.health_connect.playground.HCPlaygroundViewModel
import com.rookmotion.rookconnectdemo.ui.selector.SelectorViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SelectorViewModel::class.java)) {
            return SelectorViewModel(
                provider = serviceLocator.authorizationProvider,
                manager = serviceLocator.rookUsersManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(HCPermissionsViewModel::class.java)) {
            return HCPermissionsViewModel(
                healthConnect = serviceLocator.rookHealthConnectManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(HCPlaygroundViewModel::class.java)) {
            return HCPlaygroundViewModel(
                transmission = serviceLocator.rookTransmissionManager,
                manager = serviceLocator.rookHealthConnectManager,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}