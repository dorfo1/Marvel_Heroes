package br.com.marvelheroes

import br.com.marvelheroes.core.extensions.resolveRetrofit
import br.com.marvelheroes.data.datasources.remote.MarvelApi
import br.com.marvelheroes.data.interactor.FavoriteHeroImpl
import br.com.marvelheroes.data.interactor.FetchHeroesImpl
import br.com.marvelheroes.data.interactor.GetFavoriteHeroesImpl
import br.com.marvelheroes.data.repository.HeroesRepositoryImpl
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.FetchHeroes
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import br.com.marvelheroes.domain.repository.HeroesRepository
import br.com.marvelheroes.presentation.detail.HeroDetailViewModel
import br.com.marvelheroes.presentation.favorites.HeroesFavoritesViewModel
import br.com.marvelheroes.presentation.heroes.HeroesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val dependencies = module {
        single<MarvelApi> { resolveRetrofit() }
        single<HeroesRepository> { HeroesRepositoryImpl(get(), get()) }

        factory<FetchHeroes> { FetchHeroesImpl(get()) }
        factory<FavoriteHero> { FavoriteHeroImpl(get()) }
        factory<GetFavoriteHeroes> { GetFavoriteHeroesImpl(get()) }

        viewModel { HeroDetailViewModel(get()) }
        viewModel { HeroesFavoritesViewModel(get(),get()) }
        viewModel { HeroesViewModel(get(),get()) }
    }
}