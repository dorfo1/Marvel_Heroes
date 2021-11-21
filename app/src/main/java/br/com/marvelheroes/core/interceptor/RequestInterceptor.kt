package br.com.marvelheroes.core.interceptor

import br.com.marvelheroes.core.exceptions.UIException
import br.com.marvelheroes.core.util.ConnectionHelper
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class RequestInterceptor  : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if(!ConnectionHelper.isNetworkConnected()) throw IOException(UIException.NoInternetException())
        return chain.proceed(request)
    }
}