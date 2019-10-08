package com.johnriggsdev.inmarandroid.model

import com.johnriggsdev.inmarandroid.utils.Constants.Companion.LISTINGS_LATEST_URL
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.METADATA_URL
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface CryptoCurrencyApi {
    @GET("$LISTINGS_LATEST_URL")
    fun getTopFiftyCurrencies(@HeaderMap headers : Map<String, String>, @Query("start") start: String,
                              @Query("limit") limit: String, @Query("sort") sort: String,
                              @Query("convert_id") convertId: String) : Single<List<Currency>>

    @GET("$METADATA_URL")
    fun getMetadataForIDs(@HeaderMap headers : Map<String, String>, @Query("id") id: String) : Single<Map<String, CurrencyMetadata>>

}