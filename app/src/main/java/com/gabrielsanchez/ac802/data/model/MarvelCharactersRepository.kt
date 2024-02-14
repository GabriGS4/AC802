package com.gabrielsanchez.ac802.data.model

import com.gabrielsanchez.ac802.network.MarvelApiService

interface MarvelCharactersRepository {
    suspend fun getResults(apiKey: String, hash: String, limit: Int, offset: Int): RemoteCharactersResult
    suspend fun getCharacterDetails(apiKey: String, hash: String, id: Int): RemoteCharactersResult
    suspend fun getComicDetails(apiKey: String, hash: String, id: Int): RemoteCharactersResult
    suspend fun getSerieDetails(apiKey: String, hash: String, id: Int): RemoteCharactersResult
}

class NetworkMarvelCharactersRepository(private val service: MarvelApiService) : MarvelCharactersRepository {
    override suspend fun getResults(apiKey: String, hash: String, limit: Int, offset: Int): RemoteCharactersResult {
        return service.getResults(apiKey, hash, limit, offset)
    }
    override suspend fun getCharacterDetails(apiKey: String, hash: String, id: Int): RemoteCharactersResult {
        return service.getCharacterDetails(apiKey, hash, id)
    }
    override suspend fun getComicDetails(apiKey: String, hash: String, id: Int): RemoteCharactersResult {
        return service.getComicDetails(apiKey, hash, id)
    }
    override suspend fun getSerieDetails(apiKey: String, hash: String, id: Int): RemoteCharactersResult {
        return service.getSerieDetails(apiKey, hash, id)
    }
}