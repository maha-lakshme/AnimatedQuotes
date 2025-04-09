package com.maha.animatedquotes.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    private const val PEXELS_BASE_URL = "https://api.pexels.com/v1/"
        private const val QUOTES_BASE_URL = "https://quoteslate.vercel.app/"

    private val pexelsRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(PEXELS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }) // Logging interceptor for debugging network requests
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val request = original.newBuilder()
                            .header("Authorization", "z54ItlsgEc2BsYo8g6VeKZssWeoTcEpk8KMoVEiA5SDMqzKOcWZIUGs6") // Replace with your API key
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
    }


    private val quotesRetrofit: Retrofit by lazy {
        Retrofit.Builder().client(okHttpClient)
            .baseUrl(QUOTES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pexelsService: PexelsService by lazy {
        pexelsRetrofit.create(PexelsService::class.java)
    }

    val quotesService: QuotesService by lazy {
        quotesRetrofit.create(QuotesService::class.java)
    }
}