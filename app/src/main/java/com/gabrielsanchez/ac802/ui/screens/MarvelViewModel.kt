package com.gabrielsanchez.ac802.ui.screens

import android.text.Spannable.Factory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gabrielsanchez.ac802.data.model.Comics
import com.gabrielsanchez.ac802.data.model.Item
import com.gabrielsanchez.ac802.data.model.MarvelCharactersRepository
import com.gabrielsanchez.ac802.data.model.RemoteCharactersResult
import com.gabrielsanchez.ac802.ui.MarvelCharactersApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MarvelUiState {
    data class Success(val results: RemoteCharactersResult) : MarvelUiState
    object Error : MarvelUiState
    object Loading : MarvelUiState
}

class MarvelViewModel(private val marvelRepository: MarvelCharactersRepository) : ViewModel() {
    var api_key = "53557f0a483061ca4166329dfafcb05a"

    var hash = "6c2dc921a3de99a5837944f4f9e8579b"


    var marvelUiState: MarvelUiState by mutableStateOf(MarvelUiState.Loading)
        private set

    var loading: Boolean by mutableStateOf(false)
        private set

    var loadingComics: Boolean by mutableStateOf(false)
        private set

    var loadingSeries: Boolean by mutableStateOf(false)
        private set

    var comicsResults by mutableStateOf<List<RemoteCharactersResult>>(emptyList())
        private set

    var seriesResults by mutableStateOf<List<RemoteCharactersResult>>(emptyList())
        private set

    var comicsResultsMap by mutableStateOf<HashMap<String, RemoteCharactersResult>>(hashMapOf())
    var seriesResultsMap by mutableStateOf<HashMap<String, RemoteCharactersResult>>(hashMapOf())

    init {
        getMarvelCharacters()
    }

    fun getMarvelCharacters(limit: Int = 20, offset: Int = 0) {
        viewModelScope.launch {
            marvelUiState = MarvelUiState.Loading
            marvelUiState = try {
                val listResult = marvelRepository.getResults(
                    api_key,
                    hash,
                    limit,
                    offset
                )
                MarvelUiState.Success(listResult)
            } catch (e: IOException) {
                MarvelUiState.Error
            } catch (e: HttpException) {
                MarvelUiState.Error
            }
        }
    }

    fun loadMoreCharacters(offset: Int) {
        if (loading) return // No hacer nada si ya se está cargando

        viewModelScope.launch {
            loading = true // Marcar como cargando antes de iniciar la carga
            try {
                val listResult = marvelRepository.getResults(
                    api_key,
                    hash,
                    limit = 20,
                    offset = offset
                )
                if (listResult.data.results.isNotEmpty()) {
                    // Agregar los nuevos personajes a la lista existente
                    val updatedResults =
                        (marvelUiState as? MarvelUiState.Success)?.results?.data?.results.orEmpty() + listResult.data.results
                    marvelUiState =
                        MarvelUiState.Success(listResult.copy(data = listResult.data.copy(results = updatedResults)))
                }
            } catch (e: IOException) {
                // Manejar la excepción en caso de error de red
            } catch (e: HttpException) {
                // Manejar la excepción en caso de error del servidor
            } finally {
                loading = false // Marcar como no cargando después de la carga
            }
        }
    }

    fun getCharacterDetails(id: Int) {
        viewModelScope.launch {
            marvelUiState = try {
                val characterDetails = marvelRepository.getCharacterDetails(
                    api_key,
                    hash,
                    id
                )
                MarvelUiState.Success(characterDetails)
            } catch (e: IOException) {
                MarvelUiState.Error
            } catch (e: HttpException) {
                MarvelUiState.Error
            }
        }
    }

    fun getComics(comics: List<String>) {
        if (loadingComics) return // No hacer nada si ya se está cargando

        viewModelScope.launch {
            loadingComics = true
            try {
                val results = mutableListOf<RemoteCharactersResult>()
                for (id in comics) {
                    val comicDetails = comicsResultsMap[id] ?: marvelRepository.getComicDetails(
                        api_key,
                        hash,
                        id.toInt()
                    )
                    if (comicDetails.data.results.isNotEmpty()) {
                        results.add(comicDetails)
                        comicsResultsMap[id] = comicDetails
                    }
                }
                comicsResults = results
            } catch (e: IOException) {
                MarvelUiState.Error
            } catch (e: HttpException) {
                MarvelUiState.Error
            } finally {
                loadingComics = false
            }
        }
    }

    fun getSeries(series: List<String>) {
        if (loadingSeries) return // No hacer nada si ya se está cargando

        viewModelScope.launch {
            loadingSeries = true
            try {
                var results = mutableListOf<RemoteCharactersResult>()
                for (id in series) {
                    val seriesDetails = seriesResultsMap[id] ?: marvelRepository.getSerieDetails(
                        api_key,
                        hash,
                        id.toInt()
                    )
                    if (seriesDetails.data.results.isNotEmpty()) {
                        results.add(seriesDetails)
                        seriesResultsMap[id] = seriesDetails
                    }
                }
                seriesResults = results
            } catch (e: IOException) {
                MarvelUiState.Error
            } catch (e: HttpException) {
                MarvelUiState.Error
            } finally {
                loadingSeries = false
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarvelCharactersApplication)
                val marvelCharactersRepository = application.container.marvelRepository
                MarvelViewModel(marvelCharactersRepository)
            }
        }
    }
}