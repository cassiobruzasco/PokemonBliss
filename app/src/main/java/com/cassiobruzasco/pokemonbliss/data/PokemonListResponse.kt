package com.cassiobruzasco.pokemonbliss.data

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("results") val result: MutableList<PokemonListResponseItem>
)