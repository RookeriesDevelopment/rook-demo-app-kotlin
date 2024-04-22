package com.rookmotion.rookconnectdemo.features.connectionspage

import com.rookmotion.rook.sdk.domain.model.DataSource

data class ConnectionsPageState(
    val loading: Boolean = false,
    val error: String? = null,
    val dataSources: List<DataSource> = emptyList(),
    val webViewUrl: String? = null,
)
