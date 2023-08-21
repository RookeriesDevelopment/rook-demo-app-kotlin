package com.rookmotion.rookconnectdemo.features.connectionspage.domain.repository

import com.rookmotion.rookconnectdemo.features.connectionspage.domain.model.DataSource

interface DataSourceRepository {
    suspend fun getDataSources(clientUUID: String, userID: String): List<DataSource>
}