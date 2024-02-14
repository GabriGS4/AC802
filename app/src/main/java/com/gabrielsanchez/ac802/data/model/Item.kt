package com.gabrielsanchez.ac802.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class Item(
    @SerializedName("name")
    val name: String,
    @SerializedName("resourceURI")
    val resourceURI: String
)