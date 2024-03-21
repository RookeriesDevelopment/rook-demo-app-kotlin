package com.rookmotion.rookconnectdemo.di

import android.content.Context
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rookconnectdemo.common.isDebug
import io.tryrook.connectionspage.data.repository.DefaultDataSourceRepository
import io.tryrook.connectionspage.data.server.ConnectionsPageApiService
import io.tryrook.connectionspage.data.server.ConnectionsPageClient
import io.tryrook.connectionspage.domain.repository.DataSourceRepository
import kotlinx.coroutines.Dispatchers

class ServiceLocator(context: Context) {

    val defaultDispatcher = Dispatchers.IO

    val connectionsPageUrl = if (isDebug) {
        "https://connections.rook-connect.review/"
    } else {
        "https://connections.rook-connect.com/"
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
