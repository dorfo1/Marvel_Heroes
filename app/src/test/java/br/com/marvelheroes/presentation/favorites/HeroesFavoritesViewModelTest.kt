package br.com.marvelheroes.presentation.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import br.com.marvelheroes.util.MainCoroutineRule
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declareModule
import java.lang.Exception

@ExperimentalCoroutinesApi
class HeroesFavoritesViewModelTest : KoinTest {

    private lateinit var heroesFavoritesViewModel: HeroesFavoritesViewModel

    private val favoriteHero: FavoriteHero = mockk()
    private val getFavoriteHero: GetFavoriteHeroes = mockk()

    @get:Rule
    val testRule = InstantTaskExecutorRule()


    @get:Rule
    val coroutineRule = MainCoroutineRule()

    init {
        startKoin {
            declareModule {
                single { favoriteHero }
                single { getFavoriteHero }
            }
        }
    }

    @Before
    fun setup() {
        heroesFavoritesViewModel = HeroesFavoritesViewModel(get(), get())
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `favorite heroes should be success when use case returns success`() {
        val favoriteHeroes = heroesFavoritesViewModel.favoriteHeroes.test()

        coEvery { getFavoriteHero.invoke(Unit) } returns flow { emit(Resource.Success(emptyList<HeroEntity>())) }
        heroesFavoritesViewModel.getFavorites()
        verify(exactly = 1) { coroutineRule.launch { getFavoriteHero.invoke(Unit) } }

        favoriteHeroes.assertValue {
            it is Resource.Success
        }
    }

    @Test
    fun `favorite heroes should be error when use case returns error`() {
        val favoriteHeroes = heroesFavoritesViewModel.favoriteHeroes.test()

        coEvery { getFavoriteHero.invoke(Unit) } returns flow {
            emit(Resource.Error<List<HeroEntity>>(Exception()))
        }
        heroesFavoritesViewModel.getFavorites()
        verify(exactly = 1) { coroutineRule.launch { getFavoriteHero.invoke(Unit) } }

        favoriteHeroes.assertValue {
            it is Resource.Error
        }
    }

    @Test
    fun `Hero favorite should be success when use case returns success`() {
        val heroFavorite = heroesFavoritesViewModel.heroFavorite.test()
        val heroEntity = mockk<HeroEntity>()

        coEvery { favoriteHero.invoke(any()) } returns flow { emit(Resource.Success(Unit)) }
        heroesFavoritesViewModel.favorite(heroEntity, 1)
        verify(exactly = 1) { coroutineRule.launch { favoriteHero.invoke(any()) } }

        heroFavorite.assertValue {
            it is Resource.Success && it.data == 1
        }
    }

    @Test
    fun `Hero favorite should be error when use case returns error`() {
        val heroFavorite = heroesFavoritesViewModel.heroFavorite.test()
        val heroEntity = mockk<HeroEntity>()

        coEvery { favoriteHero.invoke(any()) } returns flow { emit(Resource.Error<Unit>(Exception())) }
        heroesFavoritesViewModel.favorite(heroEntity, 1)
        verify(exactly = 1) { coroutineRule.launch { favoriteHero.invoke(any()) } }

        heroFavorite.assertValue {
            it is Resource.Error
        }
    }


}