package com.rookmotion.rookconnectdemo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookmotion.rook.sdk.RookEventManager
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.RookSummaryManager
import com.rookmotion.rookconnectdemo.features.connectionspage.ConnectionsPageViewModel
import com.rookmotion.rookconnectdemo.features.sdkconfiguration.SDKConfigurationViewModel
import com.rookmotion.rookconnectdemo.features.sdkplayground.SDKPlaygroundViewModel
import com.rookmotion.rookconnectdemo.features.yesterdaysyncpermissions.YesterdaySyncPermissionsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectionsPageViewModel::class.java)) {
            return ConnectionsPageViewModel(
                dispatcher = serviceLocator.defaultDispatcher,
                connectionsPageUrl = serviceLocator.connectionsPageUrl,
                dataSourceRepository = serviceLocator.dataSourceRepository,
            ) as T
        }

        if (modelClass.isAssignableFrom(SDKConfigurationViewModel::class.java)) {
            return SDKConfigurationViewModel(
                rookConfigurationManager = serviceLocator.rookConfigurationManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(SDKPlaygroundViewModel::class.java)) {
            return SDKPlaygroundViewModel(
                rookHealthPermissionsManager = RookHealthPermissionsManager(serviceLocator.rookConfigurationManager),
                rookSummaryManager = RookSummaryManager(serviceLocator.rookConfigurationManager),
                rookEventManager = RookEventManager(serviceLocator.rookConfigurationManager),
            ) as T
        }

        if (modelClass.isAssignableFrom(YesterdaySyncPermissionsViewModel::class.java)) {
            return YesterdaySyncPermissionsViewModel(
                rookHealthPermissionsManager = RookHealthPermissionsManager(serviceLocator.rookConfigurationManager),
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}