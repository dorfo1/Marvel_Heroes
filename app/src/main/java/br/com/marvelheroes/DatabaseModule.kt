package br.com.marvelheroes

import android.content.Context
import androidx.room.Room
import br.com.marvelheroes.data.datasources.local.HeroesDao
import br.com.marvelheroes.data.datasources.local.database.MarvelHeroesDatabase
import org.koin.dsl.module

object DatabaseModule {

    private const val DATABASE_NAME = "marvel_heroes_database"

    val dependencies = module {
        single<MarvelHeroesDatabase> { provideDatabase(get()) }
        single<HeroesDao> { get<MarvelHeroesDatabase>().heroesDao() }
    }


    private fun provideDatabase(context: Context): MarvelHeroesDatabase =
        Room.databaseBuilder(context, MarvelHeroesDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
}