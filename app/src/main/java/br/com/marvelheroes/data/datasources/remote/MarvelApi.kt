package br.com.marvelheroes.data.datasources.remote


import androidx.annotation.Nullable
import br.com.marvelheroes.data.model.MarvelHeroes
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("/v1/public/characters")
    suspend fun fetchHeroes(
        @Query("offset") offset : Int,
        @Query("limit") limit : Int,
        @Query("nameStartsWith") name : String? = null
    ) : MarvelHeroes
}