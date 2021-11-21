package br.com.marvelheroes.presentation.heroes

import br.com.marvelheroes.data.datasources.local.HeroesDao
import br.com.marvelheroes.data.datasources.remote.MarvelApi
import br.com.marvelheroes.data.interactor.FetchHeroesImpl
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.data.model.Thumbnail
import br.com.marvelheroes.data.repository.HeroesRepositoryImpl
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.FetchHeroes
import br.com.marvelheroes.domain.repository.HeroesRepository
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest

class HeroesFragmentTest : KoinTest {

    private val favoriteHero = mockk<FavoriteHero>()
    private val dao = mockk<HeroesDao>()
    private val api = mockk<MarvelApi>(relaxed = true)


    private val modules = module(override = true) {
        single { favoriteHero }
        single { dao }
        single { api }
        single<HeroesRepository> { HeroesRepositoryImpl(get(), get()) }
        single<FetchHeroes> { FetchHeroesImpl(get()) }
        viewModel<HeroesViewModel> { HeroesViewModel(get(), get()) }
    }

    @Before
    fun setup() {
        loadKoinModules(modules)
    }

    @After
    fun tearDown() {
        unloadKoinModules(modules)
    }

    @Test
    fun displayEmptyListWhenFetchIsSuccess() {
        execute {
            withFetchHeroesSuccess(emptyList())
        } execute {} check {
            checkInfoMessageIsShowing()
            checkTextDisplayed("Nenhum herói encontrato")
        }
    }

    @Test
    fun displayErrorMessageWhenFetchIsFailure() {
        execute {
            withFetchHeroesError()
        } execute {} check {
            checkInfoMessageIsShowing()
            checkTextDisplayed("Não foi póssivel encontrar heróis, tente novamente mais tarde")
        }
    }

    @Test
    fun displayHeroOnListWhenFetchIsSuccess() {
        val list = listOf(HeroModel.Remote(0, "name", "desc", Thumbnail("path", "ext")))
        execute {
            withFetchHeroesSuccess(list)
        } execute {} check {
            checkRecyclerViewIsDisplayed()
            checkInfoMessageIsNotShowing()
            checkRecyclerViewContainsItem("name")
        }
    }
}