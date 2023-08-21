package com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConnectionsPageDTO(
    @Json(name = "client_name")
    val clientName: String,
    @Json(name = "data_sources")
    val dataSources: List<DataSourceDTO>,
)
