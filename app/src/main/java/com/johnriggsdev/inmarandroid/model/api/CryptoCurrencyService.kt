package com.johnriggsdev.inmarandroid.model.api

import com.johnriggsdev.inmarandroid.utils.Constants.Companion.BASE_URL
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CryptoCurrencyService {
    val api : CryptoCurrencyApi

    init {
        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BASIC
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(CryptoCurrencyApi::class.java)
    }

    fun getTopFiftyCurrencies(headers : Map<String, String>,
                                  limit: String, sort: String) : Single<ApiCurrencyResponse> {
        return api.getTopFiftyCurrencies(headers, limit, sort)
    }

    fun getMetadataForIDs(headers : Map<String, String>, id: String) : Single<ApiMetadataResponse> {
        return api.getMetadataForIDs(headers, id)
    }
}