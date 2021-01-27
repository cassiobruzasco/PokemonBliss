package com.cassiobruzasco.pokemonbliss.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.cassiobruzasco.pokemonbliss.data.api.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val repo: PokemonRepository
): BaseViewModel() {

    val model = PokemonModel()

    fun getPokemonList(limit: Int) {
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(true)
        model.pokemonLoadedOb.value = false
        viewModelScope.launch {
            val responseData = repo.getPokemonList(limit)
            val responseDataModel = handleResponse(responseData, ::handleGetPokemonError)
            responseDataModel?.let {
                model.pokemonListOb.value = it
            }
            model.pokemonLoadedOb.value = true
        }
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
    }

    fun getPokemon(id: Int) {
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(true)
        model.pokemonLoadedOb.value = false
        viewModelScope.launch {
            val responseData = repo.getPokemon(id)
            val responseDataModel = handleResponse(responseData, ::handleGetPokemonError)
            responseDataModel?.let {
                model.pokemonDetailsOb.value = it
            }
            model.pokemonLoadedOb.value = true
        }
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
    }

    private fun handleGetPokemonError(error: Throwable?) {
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
        when (error) {

            else -> model.pokemonStateOb.value = PokemonModel.PokemonState.GenericError(error?.message)
        }
    }
}