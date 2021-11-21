package br.com.marvelheroes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


sealed class HeroModel {

    data class Remote(
        val id: Long?,
        val name: String?,
        val descrption: String?,
        val thumbnail : Thumbnail?
    ) : HeroModel()


    @Entity(tableName = "hero")
    data class Local(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val name: String,
        val descrption: String,
        val thumbnail : String
    ) : HeroModel()
}

data class MarvelHeroes(
    val results: List<HeroModel.Remote>
)

data class Thumbnail(
    val path : String?,
    val extension : String?,
)