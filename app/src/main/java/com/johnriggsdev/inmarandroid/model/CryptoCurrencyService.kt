package com.johnriggsdev.inmarandroid.model

import com.johnriggsdev.inmarandroid.utils.Constants.Companion.BASE_URL
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CryptoCurrencyService {
    val api : CryptoCurrencyApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        api = retrofit.create(CryptoCurrencyApi::class.java!!)
    }

    fun getTopFiftyCurrencies(headers : Map<String, String>, start: String,
                                  limit: String, sort: String, convertId: String) : Single<List<Currency>> {
        return api.getTopFiftyCurrencies(headers, start, limit, sort, convertId)
    }

    fun getMetadataForIDs(headers : Map<String, String>, id: String) : Single<Map<String, CurrencyMetadata>> {
        return api.getMetadataForIDs(headers, id)
    }
}