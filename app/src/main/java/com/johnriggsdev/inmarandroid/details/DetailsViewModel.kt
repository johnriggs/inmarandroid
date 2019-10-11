package com.johnriggsdev.inmarandroid.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.johnriggsdev.inmarandroid.model.api.CryptoCurrencyService
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.model.CurrencyMetadata
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.HEADERS
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel : ViewModel() {
    private val currencyMetadata = MutableLiveData<CurrencyMetadata>()
    private val metadataError = MutableLiveData<String>()
    private val isProgressing = MutableLiveData<Boolean>()

    lateinit var currency : Currency

    var service = CryptoCurrencyService()

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

    fun fetchMetadata(currency: Currency) {
        isProgressing.value = true

        compositeDisposable.add(service.getMetadataForIDs(HEADERS, currency.id)
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                currencyMetadata.postValue(it.data.getValue(currency.id))
                metadataError.postValue("")
                isProgressing.postValue(false)
            }, {
                metadataError.postValue(it.localizedMessage)
                isProgressing.postValue(false)
            }))
    }

    fun onRefresh(){
        fetchMetadata(currency)
    }
}