package com.rookmotion.rookconnectdemo.features.datasources.connections

import com.rookmotion.rook.sdk.domain.model.DataSource

sealed interface ConnectionsState {
    object Loading : ConnectionsState
    data class Error(val message: String) : ConnectionsState
    data class Success(val dataSources: List<DataSource>) : ConnectionsState
}
