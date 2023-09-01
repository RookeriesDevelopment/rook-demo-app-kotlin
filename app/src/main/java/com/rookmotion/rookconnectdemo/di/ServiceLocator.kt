package com.rookmotion.rookconnectdemo.di

import android.content.Context
import com.rookmotion.rook.auth.AuthorizationProvider
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

    val connectionsPageUrl get() = "https://${BuildConfig.CONNECTIONS_PAGE}/"
    private val rookApiUrl get() = "https://${BuildConfig.ROOK_URL}/"

    val authorizationProvider: AuthorizationProvider by lazy {
        if (BuildConfig.DEBUG) {
            AuthorizationProvider(context, "ADVANCED")
        } else {
            AuthorizationProvider(context)
        }
    }

    val rookUsersManager: RookUsersManager by lazy {
        if (BuildConfig.DEBUG) {
            RookUsersManager(
                context,
                BuildConfig.ROOK_URL,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD,
                "ADVANCED"
            )
        } else {
            RookUsersManager(
                context,
                BuildConfig.ROOK_URL,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD,
            )
        }
    }

    val rookTransmissionManager: RookTransmissionManager by lazy {
        if (BuildConfig.DEBUG) {
            RookTransmissionManager(
                context,
                BuildConfig.ROOK_URL,
                BuildConfig.USER_ID,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD,
                "ADVANCED",
            )
        } else {
            RookTransmissionManager(
                context,
                BuildConfig.ROOK_URL,
                BuildConfig.USER_ID,
                BuildConfig.CLIENT_UUID,
                BuildConfig.CLIENT_PASSWORD
            )
        }
    }

    val rookHealthConnectManager: RookHealthConnectManager by lazy {
        if (BuildConfig.DEBUG) {
            RookHealthConnectManager(context, "ADVANCED")
        } else {
            RookHealthConnectManager(context)
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
