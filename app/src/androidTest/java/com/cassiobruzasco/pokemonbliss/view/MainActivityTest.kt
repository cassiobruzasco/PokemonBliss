package com.cassiobruzasco.pokemonbliss.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.cassiobruzasco.pokemonbliss.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Test
    fun mainActivity_shouldHaveToolbar() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }
}