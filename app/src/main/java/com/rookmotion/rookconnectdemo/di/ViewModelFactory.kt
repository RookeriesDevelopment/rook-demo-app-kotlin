package com.rookmotion.rookconnectdemo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.features.connectionspage.ui.ConnectionsPageViewModel
import com.rookmotion.rookconnectdemo.features.healthconnect.permissions.HCPermissionsViewModel
import com.rookmotion.rookconnectdemo.features.healthconnect.playground.HCPlaygroundViewModel
import com.rookmotion.rookconnectdemo.features.modules.AuthViewModel
import com.rookmotion.rookconnectdemo.features.modules.UserViewModel
import com.rookmotion.rookconnectdemo.features.sdk.SDKViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel() as T
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

        if (modelClass.isAssignableFrom(ConnectionsPageViewModel::class.java)) {

            val connectionsPageUrl =
                if (BuildConfig.DEBUG) "https://connections.rook-connect.review/"
                else "https://connections.rook-connect.com/"

            return ConnectionsPageViewModel(
                dispatcher = serviceLocator.defaultDispatcher,
                connectionsPageUrl = connectionsPageUrl,
                dataSourceRepository = serviceLocator.dataSourceRepository,
            ) as T
        }

        if (modelClass.isAssignableFrom(SDKViewModel::class.java)) {
            return SDKViewModel(serviceLocator.rookConfigurationManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}