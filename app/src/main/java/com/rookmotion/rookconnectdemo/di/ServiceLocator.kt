package com.rookmotion.rookconnectdemo.di

import android.content.Context
import com.rookmotion.rook.health_connect.RookHealthConnectManager
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.transmission.RookTransmissionManager
import com.rookmotion.rook.users.RookUsersManager
import com.rookmotion.rookconnectdemo.BuildConfig
import com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.ConnectionsPageApiService
import com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.ConnectionsPageClient
import com.rookmotion.rookconnectdemo.features.connectionspage.data.repository.DefaultDataSourceRepository
import com.rookmotion.rookconnectdemo.features.connectionspage.domain.repository.DataSourceRepository
import kotlinx.coroutines.Dispatchers

class ServiceLocator(context: Context) {

    val defaultDispatcher = Dispatchers.IO

    val rookUsersManager: RookUsersManager by lazy {
        RookUsersManager(
            context = context,
            clientUUID = BuildConfig.CLIENT_UUID,
            clientPassword = BuildConfig.CLIENT_PASSWORD,
        )
    }

    val rookTransmissionManager: RookTransmissionManager by lazy {
        RookTransmissionManager(
            context = context,
            userID = BuildConfig.USER_ID,
            clientUUID = BuildConfig.CLIENT_UUID,
            clientPassword = BuildConfig.CLIENT_PASSWORD,
        )
    }

    val rookHealthConnectManager: RookHealthConnectManager by lazy {
        RookHealthConnectManager(context)
    }

    private val connectionsPageApiService: ConnectionsPageApiService by lazy {
        ConnectionsPageClient().connectionsPageApiService
    }

    val dataSourceRepository: DataSourceRepository by lazy {
        DefaultDataSourceRepository(connectionsPageApiService)
    }

    val rookConfigurationManager: RookConfigurationManager by lazy {
        RookConfigurationManager(context)
    }
}
