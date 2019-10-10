package com.johnriggsdev.inmarandroid.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.johnriggsdev.inmarandroid.model.CryptoCurrencyService
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.utils.Constants
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.ACCEPT_KEY
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.API_COUNT
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.APP_JSON
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.DEFAULT_SORT
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.HEADER_KEY
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private var fetchedCurrencies = false
    var hasConnection = false

    private val currencies = MutableLiveData<Array<Currency>>()
    private val currencyError = MutableLiveData<String>()
    private val isProgressing = MutableLiveData<Boolean>()

    private val service = CryptoCurrencyService()

    private val compositeDisposable = CompositeDisposable()

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

    private fun fetchCurrencies() {
        if (!fetchedCurrencies && hasConnection) {
            isProgressing.value = true
            val headers: MutableMap<String, String> = mutableMapOf()
            headers.put(ACCEPT_KEY, APP_JSON)
            headers.put(HEADER_KEY, Constants.getApiKey())

            compositeDisposable.add(service.getTopFiftyCurrencies(headers, API_COUNT, DEFAULT_SORT)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    currencies.value = it.data
                    fetchedCurrencies = true
                    currencyError.value = ""
                    isProgressing.value = false
                }, {
                    currencyError.value = it.localizedMessage
                    isProgressing.value = false
                })
            )
        }
    }

    fun onRefresh(){
        fetchedCurrencies = false
        fetchCurrencies()
    }
}