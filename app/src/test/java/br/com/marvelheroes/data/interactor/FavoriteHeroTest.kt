package br.com.marvelheroes.data.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.repository.HeroesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declareModule
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class FavoriteHeroTest : KoinTest {


    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var favoriteHero: FavoriteHero
    private val repository: HeroesRepository = mockk<HeroesRepository>()


    init {
        startKoin {
            declareModule {
                single { repository }
            }
        }
    }

    @After
    fun clear() {
        stopKoin()
    }

    @Before
    fun setUp() {
        favoriteHero = FavoriteHeroImpl(get())
    }


    @Test
    fun `Should emit success when add to favorite and repository returns success`() {
        val heroEntity = HeroEntity(0,"name","desc","thumb",true)

        coEvery { repository.addHeroeToFavorities(HeroEntity.toLocalModel(heroEntity)) } returns Unit


        runBlocking {
            favoriteHero.invoke(heroEntity).test {
                assert(awaitItem() is Resource.Loading)
                assert(awaitItem() is Resource.Success)
                awaitComplete()
            }
        }

        coVerify(exactly = 1) { repository.addHeroeToFavorities(any()) }
        coVerify(exactly = 0) { repository.removeHeroeFromFavorities(any()) }

    }

    @Test
    fun `Should emit success when remove from favorite and repository returns success`() {
        val heroEntity = HeroEntity(0,"name","desc","thumb",false)

        coEvery { repository.removeHeroeFromFavorities(HeroEntity.toLocalModel(heroEntity)) } returns Unit

        runBlocking {
            favoriteHero.invoke(heroEntity).test {
                assert(awaitItem() is Resource.Loading)
                assert(awaitItem() is Resource.Success)
                awaitComplete()
            }
        }

        coVerify(exactly = 0) { repository.addHeroeToFavorities(any()) }
        coVerify(exactly = 1) { repository.removeHeroeFromFavorities(any()) }
    }

    @Test
    fun `Should emit error when add to favorite and repository returns error`() {
        val heroEntity = HeroEntity(0,"name","desc","thumb",true)
        coEvery { repository.addHeroeToFavorities(HeroEntity.toLocalModel(heroEntity)) } throws Exception()

        runBlocking {
            favoriteHero.invoke(heroEntity).test {
                assert(awaitItem() is Resource.Loading)
                assert(awaitItem() is Resource.Error)
                awaitComplete()
            }
        }

        coVerify(exactly = 1) { repository.addHeroeToFavorities(any()) }
        coVerify(exactly = 0) { repository.removeHeroeFromFavorities(any()) }
    }


    @Test
    fun `Should emit error when remove from favorite and repository returns error`() {
        val heroEntity = HeroEntity(0,"name","desc","thumb",false)
        coEvery { repository.addHeroeToFavorities(HeroEntity.toLocalModel(heroEntity)) } throws Exception()

        runBlocking {
            favoriteHero.invoke(heroEntity).test {
                assert(awaitItem() is Resource.Loading)
                assert(awaitItem() is Resource.Error)
                awaitComplete()
            }
        }

        coVerify(exactly = 0) { repository.addHeroeToFavorities(any()) }
        coVerify(exactly = 1) { repository.removeHeroeFromFavorities(any()) }
    }
}