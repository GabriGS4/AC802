package com.gabrielsanchez.ac802

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.gabrielsanchez.ac802.data.model.MarvelCharactersRepository
import com.gabrielsanchez.ac802.navigation.NavManager
import com.gabrielsanchez.ac802.ui.screens.MarvelViewModel
import com.gabrielsanchez.ac802.ui.theme.AC802Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AC802Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val marvelViewModel: MarvelViewModel =
                        viewModel(factory = MarvelViewModel.Factory)
                    NavManager(viewModel = marvelViewModel)
                }
            }
        }
    }
}
