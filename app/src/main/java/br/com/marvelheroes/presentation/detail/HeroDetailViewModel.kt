package br.com.marvelheroes.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HeroDetailViewModel(
    private val favoriteHero: FavoriteHero
) : ViewModel() {

    private val _favorite = MutableLiveData<Resource<Unit>>()
    val favorite : LiveData<Resource<Unit>> get() = _favorite


    fun onFavoriteHero(heroEntity: HeroEntity) {
        viewModelScope.launch {
            favoriteHero(heroEntity).collect {
                _favorite.postValue(it)
            }
        }
    }
}