package com.johnriggsdev.inmarandroid.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.johnriggsdev.inmarandroid.app.InmarApp
import com.johnriggsdev.inmarandroid.model.api.CryptoCurrencyService
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.API_COUNT
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.DEFAULT_SORT
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.HEADERS
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.PREFS_SORT_KEY
import com.johnriggsdev.inmarandroid.utils.PreferencesHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    var fetchedCurrencies = false
    var hasConnection = false

    private val currencies = MutableLiveData<Array<Currency>>()
    private val currencyError = MutableLiveData<String>()
    private val isProgressing = MutableLiveData<Boolean>()
    private val sortType = MutableLiveData<String>()

    private lateinit var prefs : SharedPreferences
    private lateinit var sortCategory : String

    var service = CryptoCurrencyService()

    private val compositeDisposable = CompositeDisposable()

    init {
        isProgressing.value = false
    }

    fun setPrefs(prefs : SharedPreferences){
        this.prefs = prefs
        sortCategory = prefs.getString(PREFS_SORT_KEY, DEFAULT_SORT)
        sortType.postValue(sortCategory)
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

    fun getSortType(): LiveData<String> {
        return sortType
    }

    fun setSortCategory(sortCat : String){
        if (!sortCategory.equals(sortCat)) {
            this.sortCategory = sortCat
            fetchedCurrencies = false
            prefs.edit().putString(PREFS_SORT_KEY, sortCat).apply()
            sortType.postValue(sortCategory)
            fetchCurrencies()
        }
    }

    fun fetchCurrencies() {
        if (!fetchedCurrencies && hasConnection) {
            isProgressing.value = true

            compositeDisposable.add(service.getTopFiftyCurrencies(HEADERS, API_COUNT, sortCategory)
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