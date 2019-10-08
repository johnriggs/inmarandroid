package com.johnriggsdev.inmarandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CurrencyMetadata (val urls : Map<String, Array<String>>,
                        val logo : String,
                        val id : String,
                        val name : String,
                        val symbol : String,
                        val slug : String,
                        val description : String,
                        @SerializedName("date_added") val dateAdded : String,
                        val tags : Array<String>,
                        val platform : String?,
                        val category : String) : Parcelable