package br.com.marvelheroes.presentation.heroes

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.marvelheroes.R
import br.com.marvelheroes.core.exceptions.UIException
import br.com.marvelheroes.data.datasources.local.HeroesDao
import br.com.marvelheroes.data.datasources.remote.MarvelApi
import br.com.marvelheroes.data.datasources.remote.paging.HeroPagingSource
import br.com.marvelheroes.data.interactor.FetchHeroesImpl
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.data.model.MarvelHeroes
import br.com.marvelheroes.data.repository.HeroesRepositoryImpl
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.FetchHeroes
import br.com.marvelheroes.presentation.heroes.adapter.HeroesAdapter
import br.com.marvelheroes.utils.atPosition
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okio.IOException
import org.hamcrest.Matchers.not
import org.koin.test.KoinTest
import org.koin.test.inject
import java.lang.Exception


fun HeroesFragmentTest.execute(func: HeroesFragmentRobot.() -> Unit) =
    HeroesFragmentRobot().apply { func() }

class HeroesFragmentRobot : KoinTest {


    private val dao: HeroesDao by inject()
    private val marvelApi: MarvelApi by inject()
    private val fetchHeroes: FetchHeroes by inject()

    fun withFetchHeroesSuccess(data: List<HeroModel.Remote>) {
        coEvery { dao.getFavoriteHeroes() } returns emptyList()
        coEvery { marvelApi.fetchHeroes(any(), any(), any()) } returns MarvelHeroes(data)
    }

    fun withFetchHeroesError() {
        coEvery { dao.getFavoriteHeroes() } returns emptyList()
        coEvery { marvelApi.fetchHeroes(any(), any(), any()) } throws Exception()
    }

    infix fun execute(func: HeroesFragmentRobot.() -> Unit): HeroesFragmentRobot {
        FragmentScenario.launchInContainer(HeroesFragment::class.java)
        return this.apply(func)
    }

    infix fun check(func: Result.() -> Unit) = Result().apply(func)

    inner class Result {
        fun checkRecyclerViewContainsItem(text : String) {
            onView(withId(R.id.rvHeroes))
                .check(matches(atPosition(0, hasDescendant(withText(text)))));
        }

        fun checkInfoMessageIsShowing() {
            onView(withId(R.id.infoMessage))
                .check(matches(ViewMatchers.isDisplayed()))
        }

        fun checkInfoMessageIsNotShowing() {
            onView(withId(R.id.infoMessage))
                .check(matches(not(ViewMatchers.isDisplayed())))
        }

        fun checkTextDisplayed(text: String) {
            onView(withId(R.id.infoMessage))
                .check(matches(ViewMatchers.withText(text)))
        }

        fun checkRecyclerViewIsDisplayed() {
            onView(withId(R.id.rvHeroes))
                .check(matches(ViewMatchers.isDisplayed()))
        }
    }

}