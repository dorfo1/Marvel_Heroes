package br.com.marvelheroes.data.datasources.local

import androidx.room.*
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.domain.entity.HeroEntity

@Dao
interface HeroesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHero(hero: HeroModel.Local)

    @Delete
    suspend fun deleteHero(hero: HeroModel.Local)

    @Query("SELECT * FROM hero")
    suspend fun getFavoriteHeroes(): List<HeroModel.Local>
}