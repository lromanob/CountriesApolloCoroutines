package com.example.primacountries.ui.repository.restapi

import android.app.Application
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*


class ApiHandler(private var application: Application) {

    private var token: String? = null

    /**
     * Interceptor to cache data and maintain it for a minute.
     *
     * If the same network request is sent within a minute,
     * the response is retrieved from cache.
     */

    companion object {
        @Volatile
        private var INSTANCE: ApiHandler? = null

        fun getInstance(
            application: Application
        ): ApiHandler {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiHandler(
                    application
                ).also { INSTANCE = it }
            }
        }
    }

    private var ONLINE_INTERCEPTOR: Interceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(chain.request())
        if (request.header("ApplyResponseCache")?.toBoolean() ?: false) {

            val maxAge = 60 // read from cache
            response.newBuilder()
                .removeHeader("ApplyResponseCache")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
        }

        response
    }


    private fun getOkHttpClient(): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder()

        val cacheSize = 1024 * 1024 * 10 //10 Mb
        okHttpClientBuilder.cache(Cache(application.cacheDir, cacheSize.toLong()))



        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        //okHttpClientBuilder.addInterceptor(ONLINE_INTERCEPTOR)

        return okHttpClientBuilder.build()
    }

    @JvmOverloads
    fun getApiService(
        useConverter: Boolean = true,
        ENDPOINT: String = "https://restcountries.eu/rest/v2/"
    ): ApiInterfaces {

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(getOkHttpClient())

        if (useConverter) {
            retrofitBuilder.addConverterFactory(MoshiConverterFactory.create())
        }

        return retrofitBuilder.build().create(ApiInterfaces::class.java)
    }

    fun buildDefaultParams(): HashMap<String, String> {

        return hashMapOf(
//            "appid" to params.getAppId(),
//            "deviceplatform" to params.getDevicePlatform(),
//            "environment" to params.getEnvironment(),
//            "appversion" to params.getAppVersion(),
//            "devicetype" to params.getDeviceType(),
//            "devicemodel" to params.getDeviceName(),
//            "deviceid" to params.getDeviceId(),
//            "marketplace" to params.getMarketPlace()
        )
    }

}
