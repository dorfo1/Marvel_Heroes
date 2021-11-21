package br.com.marvelheroes

import br.com.marvelheroes.core.interceptor.AuthInterceptor
import br.com.marvelheroes.core.interceptor.DataUnwrapInterceptor
import br.com.marvelheroes.core.interceptor.RequestInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://gateway.marvel.com/"
    private const val AUTH_INTERCEPTOR = "AUTH_INTERCEPTOR"
    private const val REQUEST_INTERCEPTOR = "REQUEST_INTERCEPTOR"
    private const val DATA_UNWRAP_INTERCEPTOR = "DATA_UNWRAP_INTERCEPTOR"

    val dependencies = module {
        single<Retrofit> { provideRetrofit(get()) }
        single<OkHttpClient> {
            provideOkHttpClient(
                get(named(AUTH_INTERCEPTOR)),
                get(named(DATA_UNWRAP_INTERCEPTOR)),
                get(named(REQUEST_INTERCEPTOR))
            )
        }
        factory<AuthInterceptor>(named(AUTH_INTERCEPTOR)) { AuthInterceptor(get()) }
        factory<RequestInterceptor>(named(REQUEST_INTERCEPTOR)) { RequestInterceptor() }
        factory<DataUnwrapInterceptor>(named(DATA_UNWRAP_INTERCEPTOR)) { DataUnwrapInterceptor() }
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    private fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        dataUnwrapInterceptor: DataUnwrapInterceptor,
        requestInterceptor: RequestInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()

        okHttpClient.addInterceptor(requestInterceptor)
        okHttpClient.addInterceptor(authInterceptor)
        okHttpClient.addInterceptor(dataUnwrapInterceptor)
        okHttpClient.addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        return okHttpClient.build()
    }
}