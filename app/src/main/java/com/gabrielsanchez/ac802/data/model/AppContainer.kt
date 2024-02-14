package com.gabrielsanchez.ac802.data.model

import com.gabrielsanchez.ac802.network.MarvelApiService
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val marvelRepository: MarvelCharactersRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://gateway.marvel.com/v1/public/"
    private val gson = GsonBuilder().create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val retrofitService: MarvelApiService by lazy {
           retrofit.create(MarvelApiService::class.java)
    }

    override val marvelRepository: MarvelCharactersRepository by lazy {
        NetworkMarvelCharactersRepository(retrofitService)
    }
}