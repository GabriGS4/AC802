package com.gabrielsanchez.ac802.data.model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("comics")
    val comics: Comics,
    @SerializedName("description")
    val description: String,
    val events: Events,
    @SerializedName("id")
    val id: Int,
    val modified: String,
    @SerializedName("name")
    val name: String,
    val resourceURI: String,
    @SerializedName("series")
    val series: Series,
    @SerializedName("stories")
    val stories: Stories,
    val thumbnail: Thumbnail,
    val urls: List<Url>,
    @SerializedName("title")
    val title: String
)