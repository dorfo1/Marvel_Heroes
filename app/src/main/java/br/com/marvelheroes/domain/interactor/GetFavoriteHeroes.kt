package br.com.marvelheroes.domain.interactor

import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.core.base.UseCase
import br.com.marvelheroes.domain.entity.HeroEntity

abstract class GetFavoriteHeroes : UseCase<Unit, Resource<List<HeroEntity>>>()