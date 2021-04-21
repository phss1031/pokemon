package com.kakao.mobility.model.remote

import com.google.gson.Gson
import com.kakao.mobility.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client(private val gSon: Gson) {
    fun createPokeMonService(): PokemonService =
        createRetrofit(BuildConfig.Host).create(PokemonService::class.java)

    fun createPokeMonSearchService(): PokemonAidService =
        createRetrofit(BuildConfig.SearchHost).create(PokemonAidService::class.java)

    private fun createRetrofit(host: String): Retrofit = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = httpLogLevel })
        .retryOnConnectionFailure(true)
        .build().let {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .client(it)
                .baseUrl(host)
                .build()
        }

    companion object {
        private val httpLogLevel
            get() = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
}