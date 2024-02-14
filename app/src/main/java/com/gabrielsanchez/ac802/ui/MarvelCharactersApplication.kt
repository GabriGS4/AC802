package com.gabrielsanchez.ac802.ui

import android.app.Application
import com.gabrielsanchez.ac802.data.model.AppContainer
import com.gabrielsanchez.ac802.data.model.DefaultAppContainer

class MarvelCharactersApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}