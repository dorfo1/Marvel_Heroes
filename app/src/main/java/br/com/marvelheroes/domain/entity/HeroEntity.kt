package br.com.marvelheroes.domain.entity

import android.os.Parcelable
import br.com.marvelheroes.data.model.HeroModel
import br.com.marvelheroes.data.model.Thumbnail
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeroEntity(
    val id: Long,
    val name: String,
    val descrption: String,
    val thumbnail: String,
    var favorite: Boolean = false
) : Parcelable {

    companion object {
        fun fromRemoteModel(heroModel: HeroModel.Remote) =
            HeroEntity(
                id = heroModel.id ?: 0,
                name = heroModel.name ?: "",
                descrption = heroModel.descrption ?: "",
                thumbnail = getThumbnail(heroModel.thumbnail)
            ).apply { favorite = false }

        fun fromLocalModel(heroModel: HeroModel.Local) =
            HeroEntity(
                id = heroModel.id,
                name = heroModel.name,
                descrption = heroModel.descrption,
                thumbnail = heroModel.thumbnail
            ).apply { favorite = true }

        fun toLocalModel(heroEntity: HeroEntity): HeroModel.Local =
            HeroModel.Local(
                id = heroEntity.id,
                name = heroEntity.name,
                descrption = heroEntity.descrption,
                thumbnail = heroEntity.thumbnail
            )

        private fun getThumbnail(thumbnail: Thumbnail?) =
            "${thumbnail?.path}/portrait_xlarge.${thumbnail?.extension}"
    }
}
