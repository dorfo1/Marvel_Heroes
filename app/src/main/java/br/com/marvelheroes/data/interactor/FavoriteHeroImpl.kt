package br.com.marvelheroes.data.interactor

import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.core.extensions.toFlowResource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.repository.HeroesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteHeroImpl(
    private val repository: HeroesRepository
) : FavoriteHero() {
    override suspend fun execute(params: HeroEntity): Flow<Resource<Unit>> = flow {
        if (params.favorite) {
            emit(repository.addHeroeToFavorities(HeroEntity.toLocalModel(params)))
        } else {
            emit(repository.removeHeroeFromFavorities(HeroEntity.toLocalModel(params)))
        }
    }.toFlowResource()
}