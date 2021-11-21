package br.com.marvelheroes.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.GetFavoriteHeroes
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HeroesFavoritesViewModel(
    private val getFavoriteHeroes: GetFavoriteHeroes,
    private val favoriteHero: FavoriteHero
) : ViewModel() {

    private val _favoriteHeroes = MutableLiveData<Resource<List<HeroEntity>>>()
    val favoriteHeroes : LiveData<Resource<List<HeroEntity>>> get() = _favoriteHeroes

    private val _heroFavorite = MutableLiveData<Resource<Int>>()
    val heroFavorite : LiveData<Resource<Int>> get() = _heroFavorite

    fun getFavorites() {
        viewModelScope.launch {
            getFavoriteHeroes(Unit).collect {
                _favoriteHeroes.postValue(it)
            }
        }
    }

    fun favorite(heroEntity: HeroEntity,position : Int) {
        viewModelScope.launch {
            favoriteHero(heroEntity).collect {
                when(it){
                    is Resource.Error -> _heroFavorite.postValue(Resource.Error(Exception()))
                    is Resource.Loading -> _heroFavorite.postValue(Resource.Loading())
                    is Resource.Success -> _heroFavorite.postValue(Resource.Success(position))
                }
            }
        }
    }
}