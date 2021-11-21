package br.com.marvelheroes.presentation.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.marvelheroes.databinding.AdapterHeroItemBinding
import br.com.marvelheroes.domain.entity.HeroEntity
import com.bumptech.glide.Glide

class FavoriteHeroesAdapter(
    private val onClick: (HeroEntity) -> Unit,
    private val onFavorite: (HeroEntity, Int) -> Unit
) : RecyclerView.Adapter<FavoriteHeroesAdapter.FavoritesHolder>() {

    var items = mutableListOf<HeroEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterHeroItemBinding.inflate(inflater, parent, false)
        return FavoritesHolder(binding, onClick, onFavorite)
    }

    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
        holder.bind(items[position])
    }

    fun removeItemFromList(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = items.size

    class FavoritesHolder(
        private val binding: AdapterHeroItemBinding,
        private val onClick: (HeroEntity) -> Unit,
        private val onFavorite: (HeroEntity, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(heroEntity: HeroEntity) {
            with(binding) {
                root.setOnClickListener { onClick.invoke(heroEntity) }


                Glide.with(image).load(heroEntity.thumbnail).into(image)
                heroName.text = heroEntity.name

                with(favoriteButton) {
                    isChecked = heroEntity.favorite
                    setOnClickListener {
                        heroEntity.favorite = isChecked
                        onFavorite.invoke(heroEntity, bindingAdapterPosition)
                    }
                }
            }
        }
    }
}