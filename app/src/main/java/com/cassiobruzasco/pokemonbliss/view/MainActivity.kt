package com.cassiobruzasco.pokemonbliss.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.di.repositories
import com.cassiobruzasco.pokemonbliss.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val modules = mutableListOf(
            repositories,
            viewModels,
        )

        startKoin {
            androidContext(applicationContext)
            modules(modules)
        }
    }
}