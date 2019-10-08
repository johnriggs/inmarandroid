package com.johnriggsdev.inmarandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
    val id : String,
    val name : String,
    val symbol : String,
    val slug : String,
    @SerializedName("cmc_rank") val cmcRank : String,
    @SerializedName("num_market_pairs") val numMarketPairs : String,
    @SerializedName("circulating_supply") val circulatingSupply : String,
    @SerializedName("total_supply") val totalSupply : String,
    @SerializedName("max_supply") val maxSupply : String,
    @SerializedName("last_updated") val lastUpdated : String,
    @SerializedName("date_added") val dateAdded : String,
    val tags : Array<String>,
    val platform : String?,
    @SerializedName("quote") val quotes : Map<String, Quote>
    ) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Currency

        if (!tags.contentEquals(other.tags)) return false

        return true
    }

    override fun hashCode(): Int {
        return tags.contentHashCode()
    }
}