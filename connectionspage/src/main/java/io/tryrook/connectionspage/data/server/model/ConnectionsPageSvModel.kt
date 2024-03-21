package io.tryrook.connectionspage.data.server.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConnectionsPageSvModel(
    @Json(name = "client_name")
    val clientName: String,
    @Json(name = "data_sources")
    val dataSources: List<DataSourceSvModel>,
)
