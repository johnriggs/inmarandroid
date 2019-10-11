package com.johnriggsdev.inmarandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Suppress("unused")
@Parcelize
class ResponseStatus (val timestamp : String,
                      @SerializedName("error_code") val errorCode : Int,
                      @SerializedName("error_message") val errorMessage : String?,
                      val elapsed : Int,
                      @SerializedName("credit_count") val creditCount : Int,
                      val notice : String?) : Parcelable