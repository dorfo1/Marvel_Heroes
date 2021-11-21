package br.com.marvelheroes.presentation.favorites

import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.data.model.Thumbnail
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest


class HeroesFavoritesFragmentTest : KoinTest {

    private val getFavorites = mockk<GetFavoriteHeroes>()
    private val favoriteHeroe = mockk<FavoriteHero>()


    private val modules = module {
        single { getFavorites }
        single { favoriteHeroe }
        viewModel<HeroesFavoritesViewModel> { HeroesFavoritesViewModel(get(), get()) }
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
    fun displayItemOnListWhenIsSuccess() {
        val list = listOf(HeroEntity(0, "name", "desc", "thumb"))
        execute {
            withGetFavoritesSuccess(list)
        } launch {} check {
            checkInfoMessageIsNotShowing()
            checkRecyclerViewIsDisplayed()
            checkRecyclerViewContainsItem("name")
        }
    }

    @Test
    fun displayEmptyWhenNoHeroesAddedToFavorite() {
        execute {
            withGetFavoritesSuccess(emptyList())
        } launch {} check {
            checkInfoMessageIsShowing()
            checkTextDisplayed("Nenhum herói encontrato")
        }
    }

    @Test
    fun displayErrorMessagenWhenIsError() {
        execute {
            withGetFavoritesError()
        } launch {} check {
            checkInfoMessageIsShowing()
            checkTextDisplayed("Não foi póssivel encontrar heróis, tente novamente mais tarde")
        }
    }
}