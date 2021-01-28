package com.cassiobruzasco.pokemonbliss.data.api

import com.cassiobruzasco.pokemonbliss.data.FavoriteModel
import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig.Companion.BASE_URL_POST
import retrofit2.Response
import retrofit2.http.*

interface FavoriteAPI {

    @POST(BASE_URL_POST)
    suspend fun markAsFav(
        @Query("id") id: Int,
        @Body body: FavoriteModel
    ): Response<Unit>
}