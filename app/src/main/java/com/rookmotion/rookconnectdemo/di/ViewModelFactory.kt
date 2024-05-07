package com.rookmotion.rookconnectdemo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookmotion.rook.sdk.RookEventManager
import com.rookmotion.rook.sdk.RookHealthPermissionsManager
import com.rookmotion.rook.sdk.RookSummaryManager
import com.rookmotion.rookconnectdemo.features.HomeViewModel
import com.rookmotion.rookconnectdemo.features.backgroundsteps.BackgroundStepsViewModel
import com.rookmotion.rookconnectdemo.features.connectionspage.ConnectionsPageViewModel
import com.rookmotion.rookconnectdemo.features.sdkconfiguration.SDKConfigurationViewModel
import com.rookmotion.rookconnectdemo.features.sdkplayground.SDKPlaygroundViewModel
import com.rookmotion.rookconnectdemo.features.yesterdaysync.YesterdaySyncViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
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

        if (modelClass.isAssignableFrom(YesterdaySyncViewModel::class.java)) {
            return YesterdaySyncViewModel(
                rookHealthPermissionsManager = RookHealthPermissionsManager(serviceLocator.rookConfigurationManager),
                rookDemoPreferences = serviceLocator.rookDemoPreferences,
            ) as T
        }

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                rookDemoPreferences = serviceLocator.rookDemoPreferences,
            ) as T
        }

        if (modelClass.isAssignableFrom(BackgroundStepsViewModel::class.java)) {
            return BackgroundStepsViewModel(
                rookStepsManager = serviceLocator.rookStepsManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(ConnectionsPageViewModel::class.java)) {
            return ConnectionsPageViewModel(
                rookDataSources = serviceLocator.rookDataSources,
                connectionsPageUrl = serviceLocator.connectionsPageUrl,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}