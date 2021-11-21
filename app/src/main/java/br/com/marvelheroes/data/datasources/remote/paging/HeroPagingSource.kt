package br.com.marvelheroes.data.datasources.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.marvelheroes.core.exceptions.UIException
import br.com.marvelheroes.core.util.ConnectionHelper
import br.com.marvelheroes.data.datasources.local.HeroesDao
import br.com.marvelheroes.data.datasources.remote.MarvelApi
import br.com.marvelheroes.domain.entity.HeroEntity
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class HeroPagingSource(
    private val name: String,
    private val marvelApi: MarvelApi,
    private val heroesDao: HeroesDao
) : PagingSource<Int, HeroEntity>() {

    companion object {
        const val START_OFFSET = 0
    }

    override fun getRefreshKey(state: PagingState<Int, HeroEntity>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HeroEntity> {
        val offset = params.key ?: START_OFFSET

        return try {
            val query = if (name.isEmpty()) null else name

            val response = marvelApi.fetchHeroes(limit = 20, offset = offset, name = query)
            val heroes = response.results.map { HeroEntity.fromRemoteModel(it) }
            // this is bad, but the time made me do it
            val localHeroes = heroesDao.getFavoriteHeroes().map { HeroEntity.fromLocalModel(it) }

            heroes.forEach { hero ->
                localHeroes.forEach { localHeroes ->
                    if (hero.id == localHeroes.id) hero.favorite = true
                }
            }

            LoadResult.Page(
                data = heroes,
                prevKey = if (offset == START_OFFSET) null else offset - 20,
                nextKey = if (heroes.isEmpty()) null else offset + 20
            )
        } catch (exception: IOException) {
            if (exception.cause is UIException.NoInternetException) {
                LoadResult.Error(exception.cause as UIException.NoInternetException)
            } else {
                LoadResult.Error(exception)
            }
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}