package com.rookmotion.rookconnectdemo.features.connectionspage.data.remote

import com.rookmotion.rookconnectdemo.features.connectionspage.data.remote.dto.ConnectionsPageDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ConnectionsPageApiService {

    @GET("$API_VERSION/client_uuid/{CLIENT_UUID}/user_id/{USER_ID}/configuration")
    suspend fun getDataSources(
        @Path("CLIENT_UUID") clientUUID: String,
        @Path("USER_ID") userID: String,
    ): Response<ConnectionsPageDTO>
}

private const val API_VERSION = "api/v1"
