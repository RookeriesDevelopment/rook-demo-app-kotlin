package com.rookmotion.rookconnectdemo.features.connectionspage.data.repository

import com.rookmotion.rookconnectdemo.features.connectionspage.data.mapper.toDomain
import com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.ConnectionsPageApiService
import com.rookmotion.rookconnectdemo.features.connectionspage.domain.model.DataSource
import com.rookmotion.rookconnectdemo.features.connectionspage.domain.repository.DataSourceRepository

class DefaultDataSourceRepository(
    private val connectionsPageApiService: ConnectionsPageApiService,
) : DataSourceRepository {

    override suspend fun getDataSources(clientUUID: String, userID: String): List<DataSource> {
        val response = connectionsPageApiService.getDataSources(clientUUID, userID)

        return response.body()?.dataSources?.map { it.toDomain() } ?: emptyList()
    }
}