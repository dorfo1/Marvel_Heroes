package br.com.marvelheroes.presentation.detail

import br.com.marvelheroes.R
import br.com.marvelheroes.domain.entity.HeroEntity
import org.junit.Test
import org.koin.test.KoinTest

class HeroDetailActivityTest : KoinTest {

    @Test
    fun checkHeroIsDisplayed() {
        execute { } launch {} check {
            checkViewIsDisplayed(R.id.heroName)
            checkViewWithText(R.id.heroName,"name")
            checkViewIsDisplayed(R.id.description)
            checkViewWithText(R.id.description,"description")
        }
    }

}