package br.com.marvelheroes.core.interceptor

import android.content.Context
import br.com.marvelheroes.R
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.StringBuilder

class AuthInterceptor(private val context: Context) : Interceptor {

    companion object {
        const val API_PARAM = "&apikey="
        const val HASH_PARAM = "&hash="
        const val TS_PARAM = "&ts="
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val newUrl = buildString {
            append(request.url)
            append("${API_PARAM}${context.getString(R.string.public_key)}")
            append("$HASH_PARAM${context.getString(R.string.hash)}")
            append("${TS_PARAM}1636296072")
        }

        request = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }
}