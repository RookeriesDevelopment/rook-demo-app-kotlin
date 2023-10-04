package com.rookmotion.rookconnectdemo.di

import android.content.Context
import com.rookmotion.rook.health_connect.RookHealthConnectManager
import com.rookmotion.rook.health_connect.domain.environment.RookHealthConnectEnvironment
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.transmission.RookTransmissionManager
import com.rookmotion.rook.transmission.domain.environment.RookTransmissionEnvironment
import com.rookmotion.rook.users.RookUsersManager
import com.rookmotion.rook.users.domain.environment.RookUsersEnvironment
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.ConnectionsPageApiService
import com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.ConnectionsPageClient
import com.rookmotion.rookconnectdemo.features.connectionspage.data.repository.DefaultDataSourceRepository
import com.rookmotion.rookconnectdemo.features.connectionspage.domain.repository.DataSourceRepository
import kotlinx.coroutines.Dispatchers

class ServiceLocator(context: Context) {

    val defaultDispatcher = Dispatchers.IO

    val connectionsPageUrl get() = "https://connections.rook-connect.review/"
    private val rookApiUrl get() = "https://api.rook-connect.review/"

    val rookUsersManager: RookUsersManager by lazy {
        if (BuildConfig.DEBUG) {
            RookUsersManager(
                context = context,
                clientUUID = BuildConfig.CLIENT_UUID,
                clientPassword = BuildConfig.CLIENT_PASSWORD,
                environment = RookUsersEnvironment.SANDBOX,
                logLevel = "ADVANCED"
            )
        } else {
            RookUsersManager(
                context = context,
                clientUUID = BuildConfig.CLIENT_UUID,
                clientPassword = BuildConfig.CLIENT_PASSWORD,
                environment = RookUsersEnvironment.PRODUCTION,
            )
        }
    }

    val rookTransmissionManager: RookTransmissionManager by lazy {
        if (BuildConfig.DEBUG) {
            RookTransmissionManager(
                context = context,
                userID = BuildConfig.USER_ID,
                clientUUID = BuildConfig.CLIENT_UUID,
                clientPassword = BuildConfig.CLIENT_PASSWORD,
                environment = RookTransmissionEnvironment.SANDBOX,
                logLevel = "ADVANCED",
            )
        } else {
            RookTransmissionManager(
                context = context,
                userID = BuildConfig.USER_ID,
                clientUUID = BuildConfig.CLIENT_UUID,
                clientPassword = BuildConfig.CLIENT_PASSWORD,
                environment = RookTransmissionEnvironment.PRODUCTION,
            )
        }
    }

    val rookHealthConnectManager: RookHealthConnectManager by lazy {
        if (BuildConfig.DEBUG) {
            RookHealthConnectManager(
                context = context,
                environment = RookHealthConnectEnvironment.SANDBOX,
                logLevel = "ADVANCED",
            )
        } else {
            RookHealthConnectManager(
                context = context,
                environment = RookHealthConnectEnvironment.PRODUCTION,
            )
        }
    }

    private val connectionsPageApiService: ConnectionsPageApiService by lazy {
        ConnectionsPageClient(rookApiUrl).connectionsPageApiService
    }

    val dataSourceRepository: DataSourceRepository by lazy {
        DefaultDataSourceRepository(connectionsPageApiService)
    }

    val rookConfigurationManager: RookConfigurationManager by lazy {
        RookConfigurationManager(context)
    }
}
