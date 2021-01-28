package com.cassiobruzasco.pokemonbliss.data.api

import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig.Companion.BASE_URL
import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig.Companion.BASE_URL_POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pokeAPI: PokemonAPI by lazy {
        retrofit.create(PokemonAPI::class.java)
    }

    val favAPI: FavoriteAPI by lazy {
        retrofit.create(FavoriteAPI::class.java)
    }
}