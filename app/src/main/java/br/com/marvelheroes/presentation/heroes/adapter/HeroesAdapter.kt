package br.com.marvelheroes.presentation.heroes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.marvelheroes.databinding.AdapterHeroItemBinding
import br.com.marvelheroes.domain.entity.HeroEntity
import com.bumptech.glide.Glide

class HeroesAdapter(private val onClick: (HeroEntity) -> Unit,private val onFavoriteClick: (HeroEntity) -> Unit) :
    PagingDataAdapter<HeroEntity, HeroesAdapter.HeroesViewHolder>(HEROES_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterHeroItemBinding.inflate(inflater, parent, false)
        return HeroesViewHolder(binding, onClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: HeroesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateHero(hero: HeroEntity) {
        snapshot().items.find { it.id == hero.id }?.favorite = hero.favorite
        notifyDataSetChanged()
    }

    class HeroesViewHolder(
        private val binding: AdapterHeroItemBinding,
        private val onClick: (HeroEntity) -> Unit,
        private val onFavoriteClick: (HeroEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HeroEntity?) {
            item?.let { hero ->
                with(binding) {
                    root.setOnClickListener { onClick.invoke(hero) }


                    Glide.with(image).load(hero.thumbnail).into(image)
                    heroName.text = hero.name

                    with(favoriteButton) {
                        isChecked = hero.favorite
                        setOnClickListener {
                            hero.favorite = isChecked
                            onFavoriteClick.invoke(hero)
                        }
                    }
                }
            }

        }
    }

    companion object {
        private val HEROES_DIFF = object : DiffUtil.ItemCallback<HeroEntity>() {
            override fun areItemsTheSame(oldItem: HeroEntity, newItem: HeroEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HeroEntity, newItem: HeroEntity): Boolean {
                return oldItem == newItem
            }

        }
    }
}