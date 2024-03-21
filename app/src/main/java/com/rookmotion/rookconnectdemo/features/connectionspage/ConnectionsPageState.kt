package com.rookmotion.rookconnectdemo.features.connectionspage

import io.tryrook.connectionspage.domain.model.DataSource

data class ConnectionsPageState(
    val loading: Boolean = false,
    val dataSources: List<DataSource> = emptyList(),
    val webViewUrl: String? = null,
)
