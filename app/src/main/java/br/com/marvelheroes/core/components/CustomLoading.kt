package br.com.marvelheroes.core.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import br.com.marvelheroes.R


class CustomLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val bgColor: Int = ContextCompat.getColor(context, R.color.loadingColor)


    init {
        orientation = VERTICAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        setBackgroundColor(bgColor)
        gravity = Gravity.CENTER
        createProgressBar()
    }

    private fun createProgressBar() {
        val progressBar = ProgressBar(context)
        progressBar.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        addView(progressBar)
    }
}
