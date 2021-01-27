package com.cassiobruzasco.pokemonbliss.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cassiobruzasco.pokemonbliss.data.PokemonDetailsResponse
import com.cassiobruzasco.pokemonbliss.data.PokemonListResponse

class PokemonModel {

    val pokemonListOb = MutableLiveData<PokemonListResponse>()
    val pokemonDetailsOb = MutableLiveData<PokemonDetailsResponse>()
    val pokemonIdOb = MutableLiveData<Int>()
    val pokemonStateOb = MutableLiveData<PokemonState>()
    val pokemonLoadedOb = MutableLiveData<Boolean>().apply { value = false }

    sealed class PokemonState {
        data class Loading(val isLoading: Boolean) : PokemonState()
        data class GenericError(val message: String?) : PokemonState()
    }
}