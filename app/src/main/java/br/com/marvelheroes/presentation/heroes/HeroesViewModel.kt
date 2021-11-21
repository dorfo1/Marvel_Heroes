package br.com.marvelheroes.presentation.heroes

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.domain.interactor.FavoriteHero
import br.com.marvelheroes.domain.interactor.FetchHeroes
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HeroesViewModel(
    private val fetchHeroes: FetchHeroes,
    private val favoriteHero: FavoriteHero,
) : ViewModel() {

    private val currentQuery = MutableLiveData("")

    private val _heroes = MutableLiveData<PagingData<HeroEntity>>()
    val heroes: LiveData<PagingData<HeroEntity>> get() = _heroes

    private val _heroFavorite = MutableLiveData<Resource<Unit>>()
    val heroFavorite : LiveData<Resource<Unit>> get() = _heroFavorite

    fun fetchHeroes(){
        val query = currentQuery.value ?: ""
        viewModelScope.launch {
            fetchHeroes.invoke(query).cachedIn(viewModelScope).collect {
                _heroes.postValue(it)
            }
        }
    }

    fun searchHeroes(query: String) {
        currentQuery.value = query
    }

    fun getCurrentQuery() = currentQuery.value ?: ""

    fun favorite(heroEntity: HeroEntity) {
        viewModelScope.launch {
            favoriteHero.invoke(heroEntity).collect {
                _heroFavorite.postValue(it)
            }
        }
    }
}