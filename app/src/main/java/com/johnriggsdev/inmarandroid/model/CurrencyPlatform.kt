package com.johnriggsdev.inmarandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyPlatform (val id: Int,
                             val name : String,
                             val symbol : String,
                             val slug : String,
                             @SerializedName("token_address") val tokenAddress : String) : Parcelable