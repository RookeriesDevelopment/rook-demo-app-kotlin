package com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataSourceDTO(
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
