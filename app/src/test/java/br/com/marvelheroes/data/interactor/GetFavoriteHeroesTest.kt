package br.com.marvelheroes.data.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
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
class GetFavoriteHeroesTest : KoinTest {


    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var getFavoriteHeroes: GetFavoriteHeroes
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
        getFavoriteHeroes = GetFavoriteHeroesImpl(get())
    }


    @Test
    fun `Should emit success when and repository returns success`() {
        coEvery { repository.getFavoriteHeroes() } returns emptyList()
        runBlocking {
            getFavoriteHeroes.invoke(Unit).test {
                assert(awaitItem() is Resource.Loading)
                assert(awaitItem() is Resource.Success)
                awaitComplete()
            }
        }

        coVerify(exactly = 1) { repository.getFavoriteHeroes() }
    }

    @Test
    fun `Should emit error when repository returns error`() {
        coEvery { repository.getFavoriteHeroes() } throws  Exception()
        runBlocking {
            getFavoriteHeroes.invoke(Unit).test {
                assert(awaitItem() is Resource.Loading)
                assert(awaitItem() is Resource.Error)
                awaitComplete()
            }
        }

        coVerify(exactly = 1) { repository.getFavoriteHeroes() }
    }
}