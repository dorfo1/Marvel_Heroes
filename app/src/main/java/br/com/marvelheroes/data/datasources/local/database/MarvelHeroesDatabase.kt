package br.com.marvelheroes.data.datasources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.marvelheroes.data.datasources.local.HeroesDao
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.domain.entity.HeroEntity

@Database(
    entities = [HeroModel.Local::class],
    version = 1
)
abstract class MarvelHeroesDatabase : RoomDatabase() {

    abstract fun heroesDao(): HeroesDao
}