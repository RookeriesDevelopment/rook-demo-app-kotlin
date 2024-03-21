package io.tryrook.connectionspage.data.server.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataSourceSvModel(
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "connected")
    val connected: Boolean,
    @Json(name = "authorization_url")
    val authorizationUrl: String?,
)
