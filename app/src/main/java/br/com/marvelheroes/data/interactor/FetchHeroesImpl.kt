package br.com.marvelheroes.data.interactor

import androidx.paging.PagingData
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FetchHeroes
import br.com.marvelheroes.domain.repository.HeroesRepository
import kotlinx.coroutines.flow.Flow

class FetchHeroesImpl(
    private val repository: HeroesRepository
) : FetchHeroes() {

    override suspend fun execute(params: String): Flow<PagingData<HeroEntity>> =
        repository.fetchHeroes(params)
}