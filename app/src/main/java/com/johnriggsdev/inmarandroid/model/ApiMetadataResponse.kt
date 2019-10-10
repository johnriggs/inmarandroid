package com.johnriggsdev.inmarandroid.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ApiMetadataResponse (val status : ResponseStatus, val data : Map<String, CurrencyMetadata>) : Parcelable