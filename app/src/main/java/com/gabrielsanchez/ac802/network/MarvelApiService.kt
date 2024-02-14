package com.gabrielsanchez.ac802.network

import com.gabrielsanchez.ac802.data.model.RemoteCharactersResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApiService {
    @GET("characters?ts=patata")
    suspend fun getResults(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): RemoteCharactersResult

    @GET("characters?ts=patata")
    suspend fun getCharacterDetails(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("id") id: Int
    ): RemoteCharactersResult

    @GET("comics?ts=patata")
    suspend fun getComicDetails(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("id") id: Int
    ): RemoteCharactersResult

    @GET("series?ts=patata")
    suspend fun getSerieDetails(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("id") id: Int
    ): RemoteCharactersResult
}