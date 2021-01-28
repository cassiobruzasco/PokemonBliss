package com.cassiobruzasco.pokemonbliss.data.api

import com.cassiobruzasco.pokemonbliss.data.FavoriteModel
import com.cassiobruzasco.pokemonbliss.data.PokemonDetailsResponse
import com.cassiobruzasco.pokemonbliss.data.PokemonListResponse

interface PokemonRepository: Repository {
    suspend fun getPokemonList(
        limit: Int
    ): Result<PokemonListResponse>

    suspend fun getPokemon(
        id: Int
    ): Result<PokemonDetailsResponse>

    suspend fun markAsFav(
        id: Int,
        body: FavoriteModel
    ): Result<Unit>
}

open class PokemonRepositoryImpl: BaseRepository(), PokemonRepository {

    override suspend fun getPokemonList(limit: Int): Result<PokemonListResponse> {
        return handleResponse(errorBodyType = PokemonError::class.java) {
            RetrofitInstance.pokeAPI.getPokemonList(
                limit = limit,
            )
        }
    }

    override suspend fun getPokemon(id: Int): Result<PokemonDetailsResponse> {
        return handleResponse(errorBodyType = PokemonError::class.java) {
            RetrofitInstance.pokeAPI.getPokemon(
                id = id
            )
        }
    }

    override suspend fun markAsFav(id: Int, body: FavoriteModel): Result<Unit> {
        return handleResponse(errorBodyType = PokemonError::class.java) {
            RetrofitInstance.favAPI.markAsFav(id, body)
        }
    }
}