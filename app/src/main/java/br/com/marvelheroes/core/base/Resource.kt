package br.com.marvelheroes.core.base

import java.lang.Exception

sealed class Resource<T>(
    var data : T? = null,
    var exception : Exception? =null
) {
    class Success<T>(data : T) : Resource<T>(data)
    class Error<T>(exception: Exception) : Resource<T>(null,exception)
    class Loading<T>() : Resource<T>()
}