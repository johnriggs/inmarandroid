package com.johnriggsdev.inmarandroid.model.api

import android.os.Parcelable
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.model.ResponseStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ApiCurrencyResponse (val status : ResponseStatus, val data : Array<Currency>) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiCurrencyResponse

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}