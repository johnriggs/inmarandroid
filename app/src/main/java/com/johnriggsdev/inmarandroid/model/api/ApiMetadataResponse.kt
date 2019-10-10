package com.johnriggsdev.inmarandroid.model.api

import android.os.Parcelable
import com.johnriggsdev.inmarandroid.model.CurrencyMetadata
import com.johnriggsdev.inmarandroid.model.ResponseStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ApiMetadataResponse (val status : ResponseStatus, val data : Map<String, CurrencyMetadata>) : Parcelable