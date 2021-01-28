package com.cassiobruzasco.pokemonbliss.repository

import com.cassiobruzasco.pokemonbliss.data.*
import com.cassiobruzasco.pokemonbliss.data.api.GenericException
import com.cassiobruzasco.pokemonbliss.data.api.PokemonRepository

class FakePokemonRepository : PokemonRepository {
    override suspend fun getPokemonList(limit: Int): Result<PokemonListResponse> {
        return Result.success(PokemonListResponse(mutableListOf(
            PokemonListResponseItem(
                name = "pokemonTest",
                url = "empty url"
            )
        )))
    }

    fun getPokemonListFailure(): Result<PokemonListResponse> {
        return Result.failure(GenericException("generic error", null))
    }

    override suspend fun getPokemon(id: Int): Result<PokemonDetailsResponse> {
        return Result.success(
            PokemonDetailsResponse(
                exp = 1,
                height = 1,
                id = 1,
                mutableListOf(
                    MovesModel(
                        move = MoveModel(name = "moveTest"),
                        version = mutableListOf(VersionModel(
                            0
                        ))
                    )),
                name = "bulbatest",
                types = mutableListOf(
                    TypesModel(
                        1,
                        TypeModel("grass")
                    )
                ),
                weight = 1,
                stats = mutableListOf(
                    StatsModel(0)
                )
            )
        )
    }

    override suspend fun markAsFav(id: Int, body: FavoriteModel): Result<Unit> {
        return Result.success(Unit)
    }
}