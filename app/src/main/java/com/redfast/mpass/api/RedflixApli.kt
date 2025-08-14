package com.redfast.mpass.api

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

@Keep
data class Thumbnail(
    @Json(name = "url") val url: String
)

@Keep
data class MovieItem(
    @Json(name = "name") val name: String?,
    @Json(name = "director") val director: String?,
    @Json(name = "duration") val duration: String?,
    @Json(name = "short-description") val shortDescription: String?,
    @Json(name = "thumbnail-landscape") val landscape: Thumbnail?,
    @Json(name = "thumbnail-portrait") val portrait: Thumbnail?,
    @Json(name = "local") val local: Boolean?
)

@Keep
data class MovieItemData(
    @Json(name = "fieldData") val items: MovieItem
)

@Keep
data class MovieItemCollection(
    @Json(name = "name") val name: String?,
    @Json(name = "orientation") val orientation: String?,
    @Json(name = "width") val width: Int?,
    @Json(name = "height") val height: Int?,
    @Json(name = "items") val items: List<MovieItemData>
)



interface IRedflixApi {
    @GET("collections/{collectionId}/items")
    suspend fun getSiteCollectionItems(@HeaderMap headers: Map<String, String>,
                                       @Path("collectionId") collectionId: String): Response<MovieItemCollection>
}

open class RedflixApi {
    companion object UnitConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *>? {
            return if (type == Unit::class.java) UnitConverter else null
        }

        private object UnitConverter : Converter<ResponseBody, Unit> {
            override fun convert(value: ResponseBody) {
                value.close()
            }
        }
    }

    private val baseUrl = "https://api.webflow.com/v2/"
    private val bearerToken = "YOUR_BEARER_TOKEN"

    private val serviceAPi: IRedflixApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .readTimeout(120, TimeUnit.SECONDS)
                .build())
        .addConverterFactory(TmdbApi)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()))
        .build().create(IRedflixApi::class.java)

    private var commonHeaders = mutableMapOf(
        "Accept" to "application/json",
        "accept-version" to "1.0.0",
        "Authorization" to "Bearer $bearerToken"
    )

    open suspend fun getSiteCollectionItems(collectionId: String): MovieItemCollection? {
        return serviceAPi.getSiteCollectionItems(commonHeaders, collectionId).body()
    }
}