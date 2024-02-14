package com.gabrielsanchez.ac802.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gabrielsanchez.ac802.ui.screens.DetailsCharactersView
import com.gabrielsanchez.ac802.ui.screens.HomeCharactersView
import com.gabrielsanchez.ac802.ui.screens.MarvelViewModel

@Composable
fun NavManager(viewModel: MarvelViewModel) {
    val navController = rememberNavController()
    val marvelViewModel: MarvelViewModel =
        viewModel(factory = MarvelViewModel.Factory)
    NavHost(navController = navController, startDestination = AppScreens.HomeCharacters.route) {
        composable(AppScreens.HomeCharacters.route) {
            HomeCharactersView(
                navController = navController
            )
        }

        composable(AppScreens.DetailsCharacter.route + "/{id}", arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )) {
            val id = it.arguments!!.getInt("id")
            DetailsCharactersView(
                navController = navController,
                marvelUiState = marvelViewModel.marvelUiState,
                retryAction = { marvelViewModel.getCharacterDetails(id) },
                viewModel = viewModel,
                id = id
            )
        }

    }
}