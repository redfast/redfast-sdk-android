package com.redfast.mpass.api

import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.userId
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val RF_APP = "DEMO_APP_ID"

data class TokenBody(
    val token: String?,
    val channel_type: String?,
    val endpoint_id: String?,
)

interface ITokenApi {
    @POST("ingest/update_push_endpoint")
    suspend fun postToken(
        @HeaderMap headers: Map<String, String>,
        @Body body: TokenBody
    ): Response<Void?>?
}

open class TokenApi {
    private val baseUrl = "https://conduit.redfastlabs.com/"

    private val serviceAPi: ITokenApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build().create(ITokenApi::class.java)

    private var commonHeaders = mutableMapOf(
        "Content-Type" to "application/json"
    )

    open suspend fun postToken(token: String, type: String): Response<Void?>? {
        val userId = DefaultSharedPrefs.userId
        val headers = mutableMapOf<String, String>().apply {
            putAll(commonHeaders)
            put("Rf-App", RF_APP)
            put("User-Id", userId)
        }
        val body = TokenBody(
            token = token,
            channel_type = type,
            endpoint_id = userId.hashCode().toString() + "_android"
        )
        return serviceAPi.postToken(headers, body)
    }
}