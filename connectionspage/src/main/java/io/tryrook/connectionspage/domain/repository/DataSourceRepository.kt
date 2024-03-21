package io.tryrook.connectionspage.domain.repository

import io.tryrook.connectionspage.domain.model.DataSource

interface DataSourceRepository {
    suspend fun getDataSources(clientUUID: String, userID: String): List<DataSource>
}