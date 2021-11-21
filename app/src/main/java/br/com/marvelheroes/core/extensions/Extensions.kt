package br.com.marvelheroes.core.extensions

import android.view.View
import br.com.marvelheroes.core.base.Resource
import kotlinx.coroutines.flow.*
import org.koin.core.scope.Scope
import retrofit2.Retrofit

inline fun <reified T> Scope.resolveRetrofit(retrofit: Retrofit = get()): T {
    return retrofit.create(T::class.java)
}

fun <T> Flow<T>.toFlowResource(): Flow<Resource<T>> = flow {
    onStart { emit(Resource.Loading<T>()) }
        .onEach { emit(Resource.Success(it)) }
        .catch { emit(Resource.Error<T>(Exception(it))) }
        .collect()
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
