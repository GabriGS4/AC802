package com.gabrielsanchez.ac802.navigation

sealed class AppScreens(val route: String) {
    object HomeCharacters : AppScreens("home")
    object DetailsCharacter : AppScreens("details")
}