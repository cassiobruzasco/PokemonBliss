package com.cassiobruzasco.pokemonbliss.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cassiobruzasco.pokemonbliss.data.api.GenericException
import com.cassiobruzasco.pokemonbliss.util.MainCoroutineRule
import com.cassiobruzasco.pokemonbliss.repository.FakePokemonRepository
import com.cassiobruzasco.pokemonbliss.util.ViewUtil
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.Rule

@ExperimentalCoroutinesApi
class PokemonViewModelTest: BaseViewModel() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: PokemonViewModel
    private lateinit var model: PokemonModel
    private lateinit var repo: FakePokemonRepository

    @Before
    fun setUp() {
        viewModel = PokemonViewModel(FakePokemonRepository())
        model = PokemonModel()
        repo = FakePokemonRepository()
    }

    private fun handleError(error: Throwable?){
        model.pokemonStateOb.value = PokemonModel.PokemonState.GenericError(error?.message)
    }

    @Test
    fun `getPokemonList should not return null`() = runBlockingTest {
        this.launch {
            val responseData = repo.getPokemonList(1)
            val responseDataModel = handleResponse(responseData, ::handleError)
            responseDataModel?.let {
                model.pokemonListOb.value = it
            }
        }
        val result = model.pokemonListOb.value
        assertThat(result).isNotNull()
    }

    @Test
    fun `getPokemonList should contain a object that has name property with pokemonTest as value`() = runBlockingTest {
        var pokemonName = ""
        this.launch {
            val responseData = repo.getPokemonList(1)
            val responseDataModel = handleResponse(responseData, ::handleError)
            responseDataModel?.let {
                model.pokemonListOb.value = it
                pokemonName = model.pokemonListOb.value?.result?.get(0)?.name ?: ""
            }
        }
        assertThat(pokemonName).isEqualTo("pokemonTest")
    }

    @Test
    fun `getPokemon should not return null`() = runBlockingTest {
        this.launch {
            val responseData = repo.getPokemon(1)
            val responseDataModel = handleResponse(responseData, ::handleError)
            responseDataModel?.let {
                model.pokemonDetailsOb.value = it
            }
        }
        val result = model.pokemonDetailsOb.value
        assertThat(result).isNotNull()
    }

    @Test
    fun `getPokemon should contain a object that has name property with bulbatest as value`() = runBlockingTest {
        var pokemonName = ""
        this.launch {
            val responseData = repo.getPokemon(1)
            val responseDataModel = handleResponse(responseData, ::handleError)
            responseDataModel?.let {
                model.pokemonDetailsOb.value = it
                pokemonName = model.pokemonDetailsOb.value?.name ?: ""
            }
        }
        assertThat(pokemonName).isEqualTo("bulbatest")
    }

    @Test
    fun `getPokemon should contain a object that has move name property with moveTest as value`() = runBlockingTest {
        var moveName = ""
        this.launch {
            val responseData = repo.getPokemon(1)
            val responseDataModel = handleResponse(responseData, ::handleError)
            responseDataModel?.let {
                model.pokemonDetailsOb.value = it
                moveName = model.pokemonDetailsOb.value?.moves?.get(0)?.move?.name ?: ""
            }
        }
        assertThat(moveName).isEqualTo("moveTest")
    }

    @Test
    fun `state should be generic error if response is failure`() = runBlockingTest {
        this.launch {
            val responseData = repo.getPokemonListFailure()
            handleResponse(responseData, ::handleError)
        }
        val result = model.pokemonStateOb.value
        assertThat(result).isEqualTo(PokemonModel.PokemonState.GenericError("generic error"))
    }
}