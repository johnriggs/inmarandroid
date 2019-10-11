package com.johnriggsdev.inmarandroid.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.johnriggsdev.inmarandroid.model.api.CryptoCurrencyService
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.API_COUNT
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.DEFAULT_SORT
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.HEADERS
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    var fetchedCurrencies = false
    var hasConnection = false

    private val currencies = MutableLiveData<Array<Currency>>()
    private val currencyError = MutableLiveData<String>()
    private val isProgressing = MutableLiveData<Boolean>()

    var service = CryptoCurrencyService()

    private val compositeDisposable = CompositeDisposable()

    init {
        isProgressing.value = false
    }

    fun updateConnection(connected : Boolean){
        hasConnection = connected
        fetchCurrencies()

        if (!hasConnection){
            isProgressing.value = false
        }
    }

    fun getCurrencies(): LiveData<Array<Currency>> {
        return currencies
    }

    fun getCurrencyError(): LiveData<String> {
        return currencyError
    }

    fun getIsProgressing(): LiveData<Boolean> {
        return isProgressing
    }

    fun fetchCurrencies() {
        if (!fetchedCurrencies && hasConnection) {
            isProgressing.value = true

            compositeDisposable.add(service.getTopFiftyCurrencies(HEADERS, API_COUNT, DEFAULT_SORT)
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    currencies.postValue(it.data)
                    fetchedCurrencies = true
                    currencyError.postValue("")
                    isProgressing.postValue(false)
                }, {
                    currencyError.postValue(it.localizedMessage)
                    isProgressing.postValue(false)
                })
            )
        }
    }

    fun onRefresh(){
        fetchedCurrencies = false
        fetchCurrencies()
    }
}