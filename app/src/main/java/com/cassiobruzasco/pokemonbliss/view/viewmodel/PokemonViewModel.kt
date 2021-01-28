package com.cassiobruzasco.pokemonbliss.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.cassiobruzasco.pokemonbliss.data.FavoriteModel
import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig
import com.cassiobruzasco.pokemonbliss.data.api.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val repo: PokemonRepository
): BaseViewModel() {

    val model = PokemonModel()

    fun getPokemonList(limit: Int) {
        model.pokemonLoadedOb.value = false
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(true)
        viewModelScope.launch {
            val responseData = repo.getPokemonList(limit)
            val responseDataModel = handleResponse(responseData, ::handleError)
            responseDataModel?.let {
                model.pokemonListOb.value = it
                model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
                model.pokemonLoadedOb.value = true
            }
        }
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
    }

    fun getPokemon(id: Int) {
        model.pokemonLoadedOb.value = false
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(true)
        viewModelScope.launch {
            val responseData = repo.getPokemon(id)
            val responseDataModel = handleResponse(responseData, ::handleError)
            responseDataModel?.let {
                model.pokemonDetailsOb.value = it
                model.pokemonLoadedOb.value = true
            }
        }
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
    }

    fun saveFavorite(id: Int, name: String) {
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(true)
        viewModelScope.launch {
            val requestModel = FavoriteModel(id = id, name = name)
            val response = repo.markAsFav(id, requestModel)
            handleResponse(response, ::handleError)
        }
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
    }

    private fun handleError(error: Throwable?) {
        model.pokemonStateOb.value = PokemonModel.PokemonState.Loading(false)
        model.pokemonStateOb.value = PokemonModel.PokemonState.GenericError(error?.message)
    }
}