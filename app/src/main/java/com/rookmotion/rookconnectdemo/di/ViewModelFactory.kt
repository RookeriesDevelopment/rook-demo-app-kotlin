package com.rookmotion.rookconnectdemo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookmotion.rookconnectdemo.features.backgroundsteps.BackgroundStepsViewModel
import com.rookmotion.rookconnectdemo.features.configuration.ConfigurationViewModel
import com.rookmotion.rookconnectdemo.features.continuousupload.ContinuousUploadViewModel
import com.rookmotion.rookconnectdemo.features.datasources.DataSourcesViewModel
import com.rookmotion.rookconnectdemo.features.datasources.connections.ConnectionsViewModel
import com.rookmotion.rookconnectdemo.features.permissions.PermissionsViewModel
import com.rookmotion.rookconnectdemo.features.sync.SyncViewModel
import com.rookmotion.rookconnectdemo.features.usermanagement.UserManagementViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ConfigurationViewModel::class.java)) {
            return ConfigurationViewModel(
                rookConfigurationManager = serviceLocator.rookConfigurationManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(BackgroundStepsViewModel::class.java)) {
            return BackgroundStepsViewModel(
                rookPermissionsManager = serviceLocator.rookPermissionsManager,
                rookStepsManager = serviceLocator.rookStepsManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(UserManagementViewModel::class.java)) {
            return UserManagementViewModel(
                rookConfigurationManager = serviceLocator.rookConfigurationManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(DataSourcesViewModel::class.java)) {
            return DataSourcesViewModel(
                rookDataSources = serviceLocator.rookDataSources,
            ) as T
        }

        if (modelClass.isAssignableFrom(ConnectionsViewModel::class.java)) {
            return ConnectionsViewModel(
                rookDataSources = serviceLocator.rookDataSources,
            ) as T
        }

        if (modelClass.isAssignableFrom(PermissionsViewModel::class.java)) {
            return PermissionsViewModel(
                rookPermissionsManager = serviceLocator.rookPermissionsManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            return SyncViewModel(
                rookSummaryManager = serviceLocator.rookSummaryManager,
                rookEventManager = serviceLocator.rookEventManager,
            ) as T
        }

        if (modelClass.isAssignableFrom(ContinuousUploadViewModel::class.java)) {
            return ContinuousUploadViewModel(
                rookPermissionsManager = serviceLocator.rookPermissionsManager,
                rookContinuousUploadManager = serviceLocator.rookContinuousUploadManager,
                rookDemoPreferences = serviceLocator.rookDemoPreferences,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}