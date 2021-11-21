package br.com.marvelheroes.domain.interactor

import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.core.base.UseCase
import br.com.marvelheroes.domain.entity.HeroEntity

abstract class FavoriteHero : UseCase<HeroEntity, Resource<Unit>>() {
}