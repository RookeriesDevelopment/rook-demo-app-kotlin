package io.tryrook.connectionspage.data.mapper

import io.tryrook.connectionspage.data.server.model.DataSourceSvModel
import io.tryrook.connectionspage.domain.model.DataSource

fun DataSourceSvModel.toDomain(): DataSource {
    return DataSource(
        name = name,
        description = description,
        thumbnail = image,
        connected = connected,
        connectionUrl = authorizationUrl,
    )
}