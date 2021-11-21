package br.com.marvelheroes.presentation.favorites

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import br.com.marvelheroes.R
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import br.com.marvelheroes.utils.atPosition
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.hamcrest.Matchers
import org.koin.test.KoinTest
import org.koin.test.inject
import java.lang.Exception

fun HeroesFavoritesFragmentTest.execute(func: HeroesFavoritesFragmentRobot.() -> Unit) =
    HeroesFavoritesFragmentRobot().apply { func() }

class HeroesFavoritesFragmentRobot : KoinTest {

    private val getFavorites: GetFavoriteHeroes by inject()


    fun withGetFavoritesSuccess(data: List<HeroEntity>) {
        coEvery { getFavorites(Unit) } returns flow { emit(Resource.Success(data)) }
    }

    fun withGetFavoritesError() {
        coEvery { getFavorites(Unit) } returns flow { emit(Resource.Error<List<HeroEntity>>(Exception())) }
    }

    infix fun launch(func: HeroesFavoritesFragmentRobot.() -> Unit): HeroesFavoritesFragmentRobot {
        FragmentScenario.launchInContainer(HeroesFavoritesFragment::class.java)
        return this.apply(func)
    }

    infix fun check(func: HeroesFavoritesFragmentRobot.Result.() -> Unit) = Result().apply(func)

    inner class Result {
        fun checkRecyclerViewContainsItem(text : String) {
            Espresso.onView(ViewMatchers.withId(R.id.rvHeroes))
                .check(
                    ViewAssertions.matches(
                        atPosition(
                            0,
                            ViewMatchers.hasDescendant(ViewMatchers.withText(text))
                        )
                    )
                );
        }

        fun checkInfoMessageIsShowing() {
            Espresso.onView(ViewMatchers.withId(R.id.infoText))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun checkInfoMessageIsNotShowing() {
            Espresso.onView(ViewMatchers.withId(R.id.infoText))
                .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        }

        fun checkTextDisplayed(text: String) {
            Espresso.onView(ViewMatchers.withId(R.id.infoText))
                .check(ViewAssertions.matches(ViewMatchers.withText(text)))
        }

        fun checkRecyclerViewIsDisplayed() {
            Espresso.onView(ViewMatchers.withId(R.id.rvHeroes))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

    }

}