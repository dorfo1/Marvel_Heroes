package br.com.marvelheroes.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import br.com.marvelheroes.R
import br.com.marvelheroes.databinding.ActivityHeroDetailBinding
import br.com.marvelheroes.domain.entity.HeroEntity
import br.com.marvelheroes.presentation.MainActivity.Companion.HERO_KEY
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeroDetailActivity : AppCompatActivity() {

    private val viewModel: HeroDetailViewModel by viewModel()

    private lateinit var heroEntity: HeroEntity

    private lateinit var binding: ActivityHeroDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.getParcelable<HeroEntity>(HERO_KEY)?.let { heroEntity = it }
            ?: kotlin.run { finish() }
        setupView()
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finishWithSuccess() }
    }

    private fun setupView() {
        with(binding) {
            Glide.with(image).load(heroEntity.thumbnail).into(image)
            heroName.text = heroEntity.name
            with(favoriteButton) {
                isChecked = heroEntity.favorite
                setOnClickListener {
                    heroEntity.favorite = isChecked
                    viewModel.onFavoriteHero(heroEntity)
                }
            }
            toolbar.setNavigationOnClickListener { finishWithSuccess() }

            description.text = if (heroEntity.descrption.isEmpty())
                getString(R.string.no_description_provided)
            else
                heroEntity.descrption
        }
    }

    override fun onBackPressed() {
        finishWithSuccess()
    }

    private fun finishWithSuccess() {
        val intent = Intent()
        intent.putExtra(HERO_KEY, heroEntity)
        setResult(RESULT_OK, intent)
        finish()
    }
}