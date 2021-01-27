package com.cassiobruzasco.pokemonbliss.view.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    fun <T> handleResponse(result: Result<T>, onFailure: (e: Throwable?) -> Unit): T? {
        return if (result.isFailure) {
            onFailure(result.exceptionOrNull())
            null
        } else {
            result.getOrNull()
        }
    }

    open fun initialize() {

    }
}