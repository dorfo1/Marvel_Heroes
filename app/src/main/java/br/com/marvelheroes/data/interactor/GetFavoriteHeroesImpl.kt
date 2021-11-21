package br.com.marvelheroes.data.interactor

import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.core.extensions.toFlowResource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import br.com.marvelheroes.domain.repository.HeroesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFavoriteHeroesImpl(
    private val heroesRepository: HeroesRepository
) : GetFavoriteHeroes() {
    override suspend fun execute(params: Unit): Flow<Resource<List<HeroEntity>>> = flow {
        val local = heroesRepository.getFavoriteHeroes()
        emit(local.map { HeroEntity.fromLocalModel(it) })
    }.toFlowResource()
}
