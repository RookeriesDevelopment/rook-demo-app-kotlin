package com.rookmotion.rookconnectdemo.features.datasources.connections

import com.rookmotion.rook.sdk.domain.enums.DataSourceType

sealed interface ConnectionsEvents {
    object DisconnectionNotSupported : ConnectionsEvents
    data class DisconnectionFailed(val dataSourceType: DataSourceType) : ConnectionsEvents
    data class DisconnectionSuccess(val dataSourceType: DataSourceType) : ConnectionsEvents
}
