package com.cassiobruzasco.pokemonbliss.di

import com.cassiobruzasco.pokemonbliss.data.api.PokemonRepository
import com.cassiobruzasco.pokemonbliss.data.api.PokemonRepositoryImpl
import com.cassiobruzasco.pokemonbliss.view.viewmodel.PokemonViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { PokemonViewModel(get()) }
}

val repositories = module {
    single<PokemonRepository> { PokemonRepositoryImpl() }
}
