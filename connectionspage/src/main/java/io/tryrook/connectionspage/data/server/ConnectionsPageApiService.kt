package io.tryrook.connectionspage.data.server

import io.tryrook.connectionspage.data.server.model.ConnectionsPageSvModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ConnectionsPageApiService {

    @GET("$API_VERSION/client_uuid/{CLIENT_UUID}/user_id/{USER_ID}/configuration")
    suspend fun getDataSources(
        @Path("CLIENT_UUID") clientUUID: String,
        @Path("USER_ID") userID: String,
    ): Response<ConnectionsPageSvModel>
}

private const val API_VERSION = "api/v1"
