package com.gabrielsanchez.ac802.data.model

import com.google.gson.annotations.SerializedName

data class Stories(
    @SerializedName("available")
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXXX>,
    val returned: Int
)