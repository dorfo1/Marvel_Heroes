package br.com.marvelheroes.presentation.favorites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.marvelheroes.R
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.core.extensions.hide
import br.com.marvelheroes.core.extensions.show
import br.com.marvelheroes.databinding.FragmentHeroesFavoritiesBinding
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.presentation.MainActivity
import br.com.marvelheroes.presentation.detail.HeroDetailActivity
import br.com.marvelheroes.presentation.favorites.adapter.FavoriteHeroesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeroesFavoritesFragment : Fragment(R.layout.fragment_heroes_favorities) {

    companion object {
        const val HERO_FAVORITE_REQUEST = 1515
    }

    private val favoriteAdapter: FavoriteHeroesAdapter by lazy {
        FavoriteHeroesAdapter(::onClickHero, ::onClickFavorite)
    }

    private fun onClickFavorite(heroEntity: HeroEntity, position: Int) {
        viewModel.favorite(heroEntity, position)
    }

    private fun onClickHero(heroEntity: HeroEntity) {
        val intent = Intent(context, HeroDetailActivity::class.java).apply {
            putExtra(MainActivity.HERO_KEY, heroEntity)
        }
        startActivityForResult(intent, HERO_FAVORITE_REQUEST)
    }


    private var _binding: FragmentHeroesFavoritiesBinding? = null
    private val binding: FragmentHeroesFavoritiesBinding get() = _binding!!
    private val viewModel: HeroesFavoritesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHeroesFavoritiesBinding.bind(view)

        with(binding) {
            with(rvHeroes) {
                adapter = favoriteAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    private fun setupObservers() {
        viewModel.favoriteHeroes.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> showMessage(getString(R.string.generic_error_message))
                is Resource.Loading -> handleLoading()
                is Resource.Success -> handleSuccess(it.data)
            }
        }

        viewModel.heroFavorite.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progress.hide()
                    Toast.makeText(
                        context,
                        getString(R.string.unable_to_favorite_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleLoading()
                is Resource.Success -> it.data?.let { position ->
                    binding.progress.hide()
                    binding.rvHeroes.show()
                    favoriteAdapter.removeItemFromList(
                        position
                    )
                    if (favoriteAdapter.itemCount == 0) showMessage(getString(R.string.empty_message))
                }
            }
        }
    }

    private fun handleSuccess(data: List<HeroEntity>?) {
        data?.let {
            if (it.isEmpty()) {
                showMessage(getString(R.string.empty_message))
                return
            }
            with(binding) {
                rvHeroes.show()
                progress.hide()
                infoText.hide()
            }
            favoriteAdapter.items = it.toMutableList()
        }
    }

    private fun handleLoading() {
        with(binding) {
            progress.show()
            infoText.hide()
        }
    }

    private fun showMessage(msg: String) {
        with(binding) {
            rvHeroes.hide()
            progress.hide()
            infoText.show()
            infoText.text = msg
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HERO_FAVORITE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data?.extras?.containsKey(MainActivity.HERO_KEY) == true) {
                val hero = data.getParcelableExtra<HeroEntity>(MainActivity.HERO_KEY)
                hero?.let {
                    if (!it.favorite) {
                        favoriteAdapter.items.forEachIndexed { index, heroEntity ->
                            if (heroEntity.id == it.id) {
                                favoriteAdapter.removeItemFromList(index)
                            }
                        }
                    }
                }

            }
        }
    }
}