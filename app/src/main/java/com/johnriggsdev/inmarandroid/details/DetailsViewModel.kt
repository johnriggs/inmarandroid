package com.johnriggsdev.inmarandroid.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.johnriggsdev.inmarandroid.model.CryptoCurrencyService
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.model.CurrencyMetadata
import com.johnriggsdev.inmarandroid.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel() : ViewModel() {
    private val currencyMetadata = MutableLiveData<CurrencyMetadata>()
    private val metadataError = MutableLiveData<String>()
    private val isProgressing = MutableLiveData<Boolean>()

    lateinit var currency : Currency

    private val service = CryptoCurrencyService()

    private val compositeDisposable = CompositeDisposable()

    fun initialize(currency: Currency){
        this.currency = currency
        fetchMetadata(currency)
    }

    fun getCurrencyMetadata() : LiveData<CurrencyMetadata>{
        return currencyMetadata
    }

    fun getMetadataError(): LiveData<String> {
        return metadataError
    }

    fun getIsProgressing(): LiveData<Boolean> {
        return isProgressing
    }

    private fun fetchMetadata(currency: Currency) {
        isProgressing.value = true
        val headers : MutableMap<String, String> = mutableMapOf()
        headers.put(Constants.ACCEPT_KEY, Constants.APP_JSON)
        headers.put(Constants.HEADER_KEY, Constants.getApiKey())

        compositeDisposable.add(service.getMetadataForIDs(headers, currency.id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                currencyMetadata.value = it.data.getValue(currency.id)
                metadataError.value = ""
                isProgressing.value = false
            }, {
                metadataError.value = it.localizedMessage
                isProgressing.value = false
            }))
    }

    fun onRefresh(){
        fetchMetadata(currency)
    }
}