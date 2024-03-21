package io.tryrook.connectionspage.data.repository

import io.tryrook.connectionspage.data.mapper.toDomain
import io.tryrook.connectionspage.data.server.ConnectionsPageApiService
import io.tryrook.connectionspage.domain.model.DataSource
import io.tryrook.connectionspage.domain.repository.DataSourceRepository

class DefaultDataSourceRepository(
    private val connectionsPageApiService: ConnectionsPageApiService,
) : DataSourceRepository {

    override suspend fun getDataSources(clientUUID: String, userID: String): List<DataSource> {
        val response = connectionsPageApiService.getDataSources(clientUUID, userID)

        return response.body()?.dataSources?.map { it.toDomain() } ?: emptyList()
    }
}