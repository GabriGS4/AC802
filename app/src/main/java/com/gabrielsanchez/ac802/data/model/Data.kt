package com.gabrielsanchez.ac802.data.model

import com.google.gson.annotations.SerializedName

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    @SerializedName("results") val results: List<Result>,
    val total: Int
)