package com.cassiobruzasco.pokemonbliss.data

import com.google.gson.annotations.SerializedName

data class PokemonListResponseItem(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
