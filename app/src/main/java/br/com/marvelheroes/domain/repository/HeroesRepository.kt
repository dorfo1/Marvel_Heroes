package br.com.marvelheroes.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import kotlinx.coroutines.flow.Flow

interface HeroesRepository {

    suspend fun fetchHeroes(query : String) : Flow<PagingData<HeroEntity>>

    suspend fun getFavoriteHeroes() : List<HeroModel.Local>

    suspend fun addHeroeToFavorities(hero: HeroModel.Local)

    suspend fun removeHeroeFromFavorities(hero: HeroModel.Local)

}