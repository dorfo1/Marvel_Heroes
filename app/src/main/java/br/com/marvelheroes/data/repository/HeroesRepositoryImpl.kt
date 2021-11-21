package br.com.marvelheroes.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.marvelheroes.data.datasources.local.HeroesDao
import br.com.marvelheroes.data.datasources.remote.MarvelApi
import br.com.marvelheroes.data.datasources.remote.paging.HeroPagingSource
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.repository.HeroesRepository
import kotlinx.coroutines.flow.Flow

class HeroesRepositoryImpl(
    private val heroesDao: HeroesDao,
    private val marvelApi: MarvelApi
) : HeroesRepository {
    override suspend fun fetchHeroes(query : String): Flow<PagingData<HeroEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { HeroPagingSource(query,marvelApi,heroesDao) }
        ).flow
    }

    override suspend fun getFavoriteHeroes(): List<HeroModel.Local> {
        return heroesDao.getFavoriteHeroes()
    }

    override suspend fun addHeroeToFavorities(hero: HeroModel.Local) {
        heroesDao.addHero(hero)
    }

    override suspend fun removeHeroeFromFavorities(hero: HeroModel.Local) {
        heroesDao.deleteHero(hero)
    }
}