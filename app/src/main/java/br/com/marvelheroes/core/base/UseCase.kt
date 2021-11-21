package br.com.marvelheroes.core.base

import androidx.paging.PagingData
import br.com.marvelheroes.domain.entity.HeroEntity
import kotlinx.coroutines.flow.Flow

abstract class UseCase<in T, out O> {

    abstract suspend fun execute(params: T): Flow<O>

    suspend operator fun invoke(params: T): Flow<O> = execute(params)
}