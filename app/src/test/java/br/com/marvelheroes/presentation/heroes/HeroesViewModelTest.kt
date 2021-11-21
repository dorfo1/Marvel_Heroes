package br.com.marvelheroes.presentation.heroes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import br.com.marvelheroes.AppModule
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.FetchHeroes
import br.com.marvelheroes.util.MainCoroutineRule
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.declareModule
import java.lang.Exception


@ExperimentalCoroutinesApi
class HeroesViewModelTest : KoinTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HeroesViewModel
    private val fetchHeroes: FetchHeroes = mockk()
    private val favoriteHero: FavoriteHero = mockk()

    init {
        startKoin {
            declareModule {
                single { fetchHeroes }
                single { favoriteHero }
            }
        }
    }

    @Before
    fun setup() {
        viewModel = HeroesViewModel(fetchHeroes, favoriteHero)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Heroes list should be success when use case returns success`() {
        val heroes = viewModel.heroes.test()

        coEvery { fetchHeroes.invoke("") } returns flow {
            emit(PagingData.from(emptyList<HeroEntity>()))
        }
        viewModel.fetchHeroes()
        verify(exactly = 1) { coroutineRule.launch { fetchHeroes.invoke("") } }

        heroes.assertHasValue()
    }

    @Test
    fun `Hero list query should be equals to user input`() {
        val heroes = viewModel.heroes.test()
        viewModel.searchHeroes("any")

        coEvery { fetchHeroes.invoke("any") } returns flow {
            emit(PagingData.from(emptyList<HeroEntity>()))
        }
        viewModel.fetchHeroes()
        verify(exactly = 1) { coroutineRule.launch { fetchHeroes.invoke("any") } }

        heroes.assertHasValue()
    }

    @Test
    fun `Hero Favorite should be success when use case returns sucess`() {
        val favorite = viewModel.heroFavorite.test()
        val heroEntity = mockk<HeroEntity>()
        coEvery { favoriteHero.invoke(any()) } returns flow { emit(Resource.Success(Unit)) }
        viewModel.favorite(heroEntity)
        verify(exactly = 1) { coroutineRule.launch { favoriteHero.invoke(any()) } }

        favorite.assertValue {
            it is Resource.Success
        }
    }

    @Test
    fun `Hero Favorite should be success when use case returns error`() {
        val favorite = viewModel.heroFavorite.test()
        val heroEntity = HeroEntity(0, "name", "desc", "thumb")
        coEvery { favoriteHero.invoke(any()) } returns flow { emit(Resource.Error<Unit>(Exception())) }
        viewModel.favorite(heroEntity)
        verify(exactly = 1) { coroutineRule.launch { favoriteHero.invoke(any()) } }

        favorite.assertValue {
            it is Resource.Error
        }
    }

}