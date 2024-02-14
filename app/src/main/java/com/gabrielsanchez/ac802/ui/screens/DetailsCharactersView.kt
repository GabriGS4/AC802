package com.gabrielsanchez.ac802.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gabrielsanchez.ac802.R
import com.gabrielsanchez.ac802.data.model.RemoteCharactersResult
import com.gabrielsanchez.ac802.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsCharactersView(
    navController: NavController,
    marvelUiState: MarvelUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MarvelViewModel,
    id: Int
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "MARVEL CHARACTERS",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                modifier = modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    colorResource(id = R.color.red_marvel)
                )
            )
        }
    ) {
        Surface {
            DetailsCharacters(
                navController = navController,
                marvelUiState = marvelUiState,
                retryAction = retryAction,
                modifier = modifier,
                contentPadding = contentPadding,
                viewModel = viewModel,
                id = id
            )
        }
    }
}

@Composable
fun DetailsCharacters(
    navController: NavController,
    marvelUiState: MarvelUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MarvelViewModel,
    id: Int
) {
    viewModel.getCharacterDetails(id)
    when (marvelUiState) {
        is MarvelUiState.Success -> CharacterDetails(marvelUiState.results, modifier, viewModel)
        is MarvelUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
        else -> {}
    }
}

fun getComics(viewModel: MarvelViewModel, comicsIds: List<String>) {
    viewModel.getComics(comicsIds)
}

fun getSeries(viewModel: MarvelViewModel, seriesIds: List<String>) {
    viewModel.getSeries(seriesIds)
}

@Composable
fun CharacterDetails(
    results: RemoteCharactersResult,
    modifier: Modifier = Modifier,
    viewModel: MarvelViewModel
) {
    val character = results.data.results[0]
    val comicsIds = character.comics.items.map { it.resourceURI.substringAfterLast("/") }
    val seriesIds = character.series.items.map { it.resourceURI.substringAfterLast("/") }
    
    getComics(viewModel, comicsIds)
    val comics = viewModel.comicsResults

    getSeries(viewModel, seriesIds)
    val series = viewModel.seriesResults

    var loadingComics = viewModel.loadingComics
    var loadingSeries = viewModel.loadingSeries
    LazyColumn(content = {
        item {
            Card(
                modifier = Modifier.padding(
                    top = 72.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    // Refactorizar la url de la imagen, recogemos el http y lo cambiamos por https
                    val imageUrl = character.thumbnail.path.replace(
                        "http",
                        "https"
                    ) + "." + character.thumbnail.extension
                    AsyncImage(
                        error = painterResource(id = R.drawable.ic_broken_image),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = character.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }
            }
            Card(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = character.name.uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 30.sp,
                        lineHeight = 90.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = Color.White
                    )
                    if (character.description.isNotEmpty()) {
                        Text(
                            text = character.description.uppercase(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp),
                            color = colorResource(id = R.color.red_marvel)
                        )
                    }
                    Text(
                        text = "EL PERSONAJE APARECE EN ${character.comics.available} COMICS",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = colorResource(id = R.color.red_marvel)
                    )
                    Text(
                        text = "EL PERSONAJE APARECE EN ${character.series.available} SERIES",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = colorResource(id = R.color.red_marvel)
                    )

                    Text(
                        text = "EL PERSONAJE APARECE EN ${character.stories.available} HISTORIAS",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = colorResource(id = R.color.red_marvel)
                    )
                }
            }
            if (character.comics.items.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "COMICS",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                if (loadingComics) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.Red,
                            strokeWidth = 5.dp,
                            trackColor = Color.Gray,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LOADING COMICS...",
                            fontSize = 20.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }

                } else {
                    ComicsList(comics = comics)
                }
            }
            if (character.series.items.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SERIES",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                if (loadingSeries) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.Red,
                            strokeWidth = 5.dp,
                            trackColor = Color.Gray,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LOADING SERIES...",
                            fontSize = 20.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    SeriesList(series = series)
                }
            }
        }
    })
}

@Composable
fun ComicsList(comics: List<RemoteCharactersResult>) {
    LazyRow(content = {
        items(comics) { item ->
            val comic = item.data.results[0]
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(150.dp, 200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter // Alinea la imagen en la parte superior del espacio disponible
                ) {
                    val imageUrl = comic.thumbnail.path.replace(
                        "http",
                        "https"
                    ) + "." + comic.thumbnail.extension
                    AsyncImage(
                        error = painterResource(id = R.drawable.ic_broken_image),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = comic.title,
                        alpha = 0.5F,
                        modifier = Modifier
                            .fillMaxWidth() // Ajusta el ancho de la imagen al máximo
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            // Ponemos el texto en mayúsculas
                            text = comic.title.uppercase(),
                            color = Color.White,
                            fontSize = 13.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    })
}

@Composable
fun SeriesList(series: List<RemoteCharactersResult>) {
    LazyRow(content = {
        items(series) { item ->
            val serie = item.data.results[0]
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(150.dp, 200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter // Alinea la imagen en la parte superior del espacio disponible
                ) {
                    val imageUrl = serie.thumbnail.path.replace(
                        "http",
                        "https"
                    ) + "." + serie.thumbnail.extension
                    AsyncImage(
                        error = painterResource(id = R.drawable.ic_broken_image),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = serie.title,
                        alpha = 0.5F,
                        modifier = Modifier
                            .fillMaxWidth() // Ajusta el ancho de la imagen al máximo
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            // Ponemos el texto en mayúsculas
                            text = serie.title.uppercase(),
                            color = Color.White,
                            fontSize = 13.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    })
}