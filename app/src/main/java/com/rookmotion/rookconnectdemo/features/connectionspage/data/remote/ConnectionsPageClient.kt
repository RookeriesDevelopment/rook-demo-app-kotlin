package com.rookmotion.rookconnectdemo.features.connectionspage.data.remote

import com.rookmotion.rookconnectdemo.common.isDebug
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ConnectionsPageClient {
    private val baseUrl = kotlin.run {
        return@run if (isDebug) {
            "https://api.rook-connect.review/"
        } else {
            "https://api.rook-connect.com/"
        }
    }

    private val requestInterceptor = Interceptor { chain ->
        val request = chain
            .request()
            .newBuilder()

        request.addHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE)
        request.addHeader(ACCEPT, DEFAULT_ACCEPT)

        return@Interceptor chain.proceed(request.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val connectionsPageApiService: ConnectionsPageApiService = retrofit.create(
        ConnectionsPageApiService::class.java
    )
}

private const val CONTENT_TYPE = "Content-Type"
private const val ACCEPT = "Accept"

private const val DEFAULT_CONTENT_TYPE = "application/json"
private const val DEFAULT_ACCEPT = "Accept"
