package br.com.marvelheroes

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarvelHeroesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MarvelHeroesApplication)
            modules(listOf(
                AppModule.dependencies,
                NetworkModule.dependencies,
                DatabaseModule.dependencies
            ))
        }
    }
}