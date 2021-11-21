package br.com.marvelheroes.presentation.heroes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.marvelheroes.R
import br.com.marvelheroes.core.base.Resource
import br.com.marvelheroes.core.exceptions.UIException
import br.com.marvelheroes.core.extensions.hide
import br.com.marvelheroes.core.extensions.show
import br.com.marvelheroes.databinding.FragmentHeroesListBinding
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.presentation.MainActivity.Companion.HERO_KEY
import br.com.marvelheroes.presentation.detail.HeroDetailActivity
import br.com.marvelheroes.presentation.heroes.adapter.HeroesAdapter
import br.com.marvelheroes.presentation.heroes.adapter.HeroesLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeroesFragment : Fragment(R.layout.fragment_heroes_list) {

    companion object {
        const val HERO_LIST_REQUEST = 1010
    }

    private val viewModel: HeroesViewModel by viewModel()

    private val heroesAdapter: HeroesAdapter by lazy {
        HeroesAdapter(::onHeroClicked, ::onFavoriteClicked)
    }

    private var _binding: FragmentHeroesListBinding? = null
    private val binding: FragmentHeroesListBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvHeroes) {
            this.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            this.adapter = heroesAdapter.withLoadStateHeaderAndFooter(
                header = HeroesLoadStateAdapter { heroesAdapter.retry() },
                footer = HeroesLoadStateAdapter { heroesAdapter.retry() }
            )
        }
        viewModel.fetchHeroes()
        setupObservers()
        setHasOptionsMenu(true)
    }

    private fun setupObservers() {
        viewModel.heroes.observe(viewLifecycleOwner) { heroes ->
            lifecycleScope.launch {
                heroesAdapter.submitData(heroes)
            }
        }

        lifecycleScope.launch {
            heroesAdapter.loadStateFlow.collectLatest {
                with(binding) {
                    progressBar.isVisible = it.source.refresh is LoadState.Loading
                    rvHeroes.isVisible = it.source.refresh is LoadState.NotLoading
                    if(it.source.refresh !is LoadState.Error) hideInfoMessage()

                    // error messaged base on exception
                    if (it.source.refresh is LoadState.Error) {
                        if ((it.refresh as LoadState.Error).error is UIException.NoInternetException) {
                            showInfoMessage(getString(R.string.no_connection))
                        } else {
                            showInfoMessage(getString(R.string.generic_error_message))
                        }
                    }

                    // empty state
                    if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && heroesAdapter.itemCount < 1){
                        rvHeroes.isVisible = false
                        showInfoMessage(getString(R.string.empty_message))
                    }
                }
            }
        }

        viewModel.heroFavorite.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> binding.progressBar.show()
                else -> binding.progressBar.hide()
            }
        }
    }

    private fun showInfoMessage(msg: String) {
        with(binding.infoMessage) {
            show()
            text = msg
        }
    }

    private fun hideInfoMessage() {
        binding.infoMessage.hide()
    }

    private fun onHeroClicked(heroEntity: HeroEntity) {
        val intent = Intent(context, HeroDetailActivity::class.java).apply {
            putExtra(HERO_KEY, heroEntity)
        }
        startActivityForResult(intent, HERO_LIST_REQUEST)

    }

    private fun onFavoriteClicked(heroEntity: HeroEntity) {
        viewModel.favorite(heroEntity)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.menu_action_search)
        val searchView = searchItem.actionView as SearchView?

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.equals(viewModel.getCurrentQuery())) return true
                binding.rvHeroes.scrollToPosition(0)
                viewModel.searchHeroes(query ?: "")
                searchView.clearFocus()
                viewModel.fetchHeroes()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) {
                    this.onQueryTextSubmit("");
                }
                return true
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HERO_LIST_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data?.extras?.containsKey(HERO_KEY) == true) {
                val hero = data.getParcelableExtra<HeroEntity>(HERO_KEY)
                hero?.let {
                    heroesAdapter.updateHero(it)
                }
            }
        }
    }
}