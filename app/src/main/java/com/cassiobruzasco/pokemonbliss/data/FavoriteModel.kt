package com.cassiobruzasco.pokemonbliss.data

import com.google.gson.annotations.SerializedName

data class FavoriteModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
