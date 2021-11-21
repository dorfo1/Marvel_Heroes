package br.com.marvelheroes.presentation.detail

import android.content.Intent
import androidx.annotation.IdRes
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import br.com.marvelheroes.domain.entity.HeroEntity

fun HeroDetailActivityTest.execute(func: HeroDetailActivityRobot.() -> Unit) =
    HeroDetailActivityRobot().apply { func() }

class HeroDetailActivityRobot {

    infix fun launch(func: HeroDetailActivityRobot.() -> Unit): HeroDetailActivityRobot {
        val heroEntity = HeroEntity(0, "name", "description", "thumbnail")
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), HeroDetailActivity::class.java)
        intent.putExtra("HERO", heroEntity) //obviously use a const for key
        ActivityScenario.launch<HeroDetailActivity>(intent)
        return this.apply(func)
    }

    infix fun check(func: HeroDetailActivityRobot.Result.() -> Unit) = Result().apply(func)

    inner class Result {

        fun checkViewIsDisplayed(@IdRes id: Int) {
            Espresso.onView(ViewMatchers.withId(id))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun checkViewWithText(@IdRes id: Int, text: String) {
            Espresso.onView(ViewMatchers.withId(id))
                .check(ViewAssertions.matches(ViewMatchers.withText(text)))
        }

    }
}