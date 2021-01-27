package com.cassiobruzasco.pokemonbliss.data.api

import com.cassiobruzasco.pokemonbliss.data.PokemonDetailsResponse
import com.cassiobruzasco.pokemonbliss.data.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {

    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("limit") limit: Int
    ): Response<PokemonListResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): Response<PokemonDetailsResponse>
}