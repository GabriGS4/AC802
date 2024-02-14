package com.gabrielsanchez.ac802.data.model

import com.google.gson.annotations.SerializedName

data class Comics(
    @SerializedName("available")
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val items: List<Item>,
    val returned: Int
)