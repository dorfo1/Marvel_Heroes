package br.com.marvelheroes.data.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.PagingSource
import app.cash.turbine.test
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.data.datasources.remote.paging.HeroPagingSource
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FetchHeroes
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import br.com.marvelheroes.domain.repository.HeroesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
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
import java.lang.Exception

class FetchHeroesTest : KoinTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var fetchHeroes: FetchHeroes
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
        fetchHeroes = FetchHeroesImpl(get())
    }


    @Test
    fun `Should emit pagingData from repository`() {
        coEvery { repository.fetchHeroes("") } returns flow { emit(PagingData.from(emptyList<HeroEntity>())) }
        runBlocking {
            fetchHeroes.invoke("").test {
                awaitItem()
                awaitComplete()
            }
        }

        coVerify(exactly = 1) { repository.fetchHeroes("") }
    }


}