package com.gabrielsanchez.ac802.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gabrielsanchez.ac802.R
import com.gabrielsanchez.ac802.data.model.RemoteCharactersResult
import com.gabrielsanchez.ac802.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCharactersView(
    navController: NavController,
) {
    Scaffold(
        modifier = Modifier,
        topBar = { MarvelTopAppBar(navController = navController) }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val marvelViewModel: MarvelViewModel =
                viewModel(factory = MarvelViewModel.Factory)
            HomeCharacters(
                navController = navController,
                marvelUiState = marvelViewModel.marvelUiState,
                retryAction = { marvelViewModel.getMarvelCharacters() },
                contentPadding = it,
                viewModel = marvelViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarvelTopAppBar(modifier: Modifier = Modifier, navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "MARVEL CHARACTERS",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontSize = 20.sp
            )
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            colorResource(id = R.color.red_marvel)
        )
    )

}

@Composable
fun HomeCharacters(
    navController: NavController,
    marvelUiState: MarvelUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MarvelViewModel
) {
    when (marvelUiState) {
        is MarvelUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is MarvelUiState.Success -> CharactersGridScreen(
            marvelUiState.results,
            navController,
            modifier,
            viewModel
        )

        is MarvelUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    LoadingSpinner("LOADING CHARACTERS...")
}


@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray) // Opcional: Fondo del Box
    ) {
        Image(
            painter = painterResource(id = R.drawable.nointernet),
            contentDescription = "",
            alpha = 0.3F,
            contentScale = ContentScale.FillBounds, // Utiliza FillBounds para que la imagen ocupe toda la pantalla
            modifier = Modifier.fillMaxSize() // Asegúrate de que la imagen ocupe toda la pantalla
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ERROR LOADING CHARACTERS",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 50.sp,
                lineHeight = 90.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.red_marvel)
            )
            Button(
                onClick = retryAction,
                modifier = Modifier
                    .padding(16.dp)
                    .height(64.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.red_marvel)
                )
            ) {
                Text(
                    text = "RETRY",
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersGridScreen(
    results: RemoteCharactersResult,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MarvelViewModel
) {
    val scrollState = rememberLazyGridState()
    val loading = viewModel.loading

    LazyVerticalGrid(
        state = scrollState,
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        // Añadimos margen superior para que no quede pegado al borde superior y no se vea por el topbar
        contentPadding = PaddingValues(top = 58.dp)
    ) {
        items(results.data.results.size) { index ->
            val character = results.data.results[index]
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                onClick = {
                    navController.navigate(AppScreens.DetailsCharacter.route + "/${character.id}")
                }
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
                        alpha = 0.5F,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
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
                            text = character.name.uppercase(),
                            color = Color.White,
                            fontSize = 13.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
            // Load more characters if scrolled to the end
            if (index == results.data.results.size - 1 && !loading && scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == index) {
                viewModel.loadMoreCharacters(results.data.offset + results.data.limit)
            }
        }
    }
    // Animación de carga al final de la lista
    if (loading) {
        LoadingSpinner("LOADING MORE CHARACTERS...")
    }

}

@Composable
fun LoadingSpinner(
    textSpinner: String = "LOADING...",
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.Red,
                strokeWidth = 5.dp,
                trackColor = Color.Gray,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = textSpinner, fontSize = 20.sp, color = Color.Red)
        }

    }
}