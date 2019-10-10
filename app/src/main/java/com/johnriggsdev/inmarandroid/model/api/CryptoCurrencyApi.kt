package com.johnriggsdev.inmarandroid.model.api

import com.johnriggsdev.inmarandroid.utils.Constants.Companion.LISTINGS_LATEST_URL
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.METADATA_URL
import io.reactivex.Single
import retrofit2.http.*

interface CryptoCurrencyApi {
    @GET("$LISTINGS_LATEST_URL")
    fun getTopFiftyCurrencies(@HeaderMap headers : Map<String, String>,
                              @Query("limit") limit: String, @Query("sort") sort: String) : Single<ApiCurrencyResponse>

    @GET("$METADATA_URL")
    fun getMetadataForIDs(@HeaderMap headers : Map<String, String>, @Query("id") id: String) : Single<ApiMetadataResponse>

}