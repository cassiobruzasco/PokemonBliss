package com.cassiobruzasco.pokemonbliss.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.di.repositories
import com.cassiobruzasco.pokemonbliss.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val modules = mutableListOf(
            repositories,
            viewModels,
        )

        startKoin {
            androidContext(applicationContext)
            modules(modules)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}