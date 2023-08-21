package com.rookmotion.rookconnectdemo.features.connectionspage.data.mapper

import com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.dto.DataSourceDTO
import com.rookmotion.rookconnectdemo.features.connectionspage.domain.model.DataSource

fun DataSourceDTO.toDomain(): DataSource {
    return DataSource(
        name = name,
        description = description,
        thumbnail = image,
        connected = connected,
        connectionUrl = authorizationUrl,
    )
}