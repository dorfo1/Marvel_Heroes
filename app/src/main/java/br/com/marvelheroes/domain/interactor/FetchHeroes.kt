package br.com.marvelheroes.domain.interactor

import androidx.paging.PagingData
import br.com.marvelheroes.core.base.UseCase
import br.com.marvelheroes.domain.entity.HeroEntity

abstract class FetchHeroes : UseCase<String, PagingData<HeroEntity>>()