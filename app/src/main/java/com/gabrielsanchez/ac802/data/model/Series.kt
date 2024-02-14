package com.gabrielsanchez.ac802.data.model

import com.google.gson.annotations.SerializedName

data class Series(
    @SerializedName("available")
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)