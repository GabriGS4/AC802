package com.gabrielsanchez.ac802.data.model

import kotlinx.serialization.SerialName

data class RemoteCharactersResult(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    @SerialName("data") val data: Data,
    val etag: String,
    val status: String
)