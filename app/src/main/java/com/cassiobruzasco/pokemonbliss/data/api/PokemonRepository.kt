package com.cassiobruzasco.pokemonbliss.data.api

import com.cassiobruzasco.pokemonbliss.data.PokemonDetailsResponse
import com.cassiobruzasco.pokemonbliss.data.PokemonListResponse

interface PokemonRepository: Repository {
    suspend fun getPokemonList(
        limit: Int
    ): Result<PokemonListResponse>

    suspend fun getPokemon(
        id: Int
    ): Result<PokemonDetailsResponse>
}

open class PokemonRepositoryImpl: BaseRepository(), PokemonRepository {

    override suspend fun getPokemonList(limit: Int): Result<PokemonListResponse> {
        return handleResponse(errorBodyType = PokemonError::class.java) {
            RetrofitInstance.api.getPokemonList(
                limit = limit,
            )
        }
    }

    override suspend fun getPokemon(id: Int): Result<PokemonDetailsResponse> {
        return handleResponse(errorBodyType = PokemonError::class.java) {
            RetrofitInstance.api.getPokemon(
                id = id
            )
        }
    }
}