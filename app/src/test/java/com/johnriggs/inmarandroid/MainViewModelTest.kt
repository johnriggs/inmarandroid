package com.johnriggs.inmarandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.johnriggsdev.inmarandroid.main.MainViewModel
import com.johnriggsdev.inmarandroid.model.api.CryptoCurrencyService
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import com.johnriggsdev.inmarandroid.utils.Constants
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.awaitility.Awaitility.await
import org.junit.ClassRule
import org.mockito.Mockito
import java.util.concurrent.Callable

class MainViewModelTest {
    @get:Rule val testRule = InstantTaskExecutorRule()
    companion object {
        @get:ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    lateinit var mockApiService : CryptoCurrencyService
    lateinit var viewModel : MainViewModel

    @Before
    fun setup(){
        mockApiService = mock()
        viewModel = MainViewModel()

        viewModel.service = mockApiService
    }

    @Test
    fun `updateConnection sets boolean`() {
        Mockito.`when`(mockApiService.getTopFiftyCurrencies(
            Constants.HEADERS,
            Constants.API_COUNT,
            Constants.DEFAULT_SORT
        )).thenReturn(Single.just(testApiCurrencyResponse))

        viewModel.updateConnection(false)
        assert(!viewModel.hasConnection)
        assert(!viewModel.getIsProgressing().value!!)

        viewModel.updateConnection(true)
        assert(viewModel.hasConnection)

        //Todo Investigate non-fatal io.reactivex.exceptions.UndeliverableException
    }

    @Test
    fun `updateConnection gets currencies`(){
        Mockito.`when`(mockApiService.getTopFiftyCurrencies(
            Constants.HEADERS,
            Constants.API_COUNT,
            Constants.DEFAULT_SORT
        )).thenReturn(Single.just(testApiCurrencyResponse))

        viewModel.updateConnection(true)

        verify(mockApiService).getTopFiftyCurrencies(Constants.HEADERS, Constants.API_COUNT, Constants.DEFAULT_SORT)

        await().until(currenciesAreAdded())

        assert(viewModel.getCurrencies().value!!.contentEquals(testApiCurrencyResponse.data))
    }

    @Test
    fun `fetchCurrencies does not get currencies if no connection or already connected`(){
        Mockito.`when`(mockApiService.getTopFiftyCurrencies(
            Constants.HEADERS,
            Constants.API_COUNT,
            Constants.DEFAULT_SORT
        )).thenReturn(Single.just(testApiCurrencyResponse))

        viewModel.updateConnection(true)

        verify(mockApiService).getTopFiftyCurrencies(Constants.HEADERS, Constants.API_COUNT, Constants.DEFAULT_SORT)

        viewModel.hasConnection = false
        viewModel.fetchCurrencies()

        verify(mockApiService, times(1)).getTopFiftyCurrencies(Constants.HEADERS, Constants.API_COUNT, Constants.DEFAULT_SORT)

        viewModel.hasConnection = true
        viewModel.fetchedCurrencies = true

        verify(mockApiService, times(1)).getTopFiftyCurrencies(Constants.HEADERS, Constants.API_COUNT, Constants.DEFAULT_SORT)
    }

    @Test
    fun `onRefresh requests currencies`(){
        Mockito.`when`(mockApiService.getTopFiftyCurrencies(
            Constants.HEADERS,
            Constants.API_COUNT,
            Constants.DEFAULT_SORT
        )).thenReturn(Single.just(testApiCurrencyResponse))

        viewModel.hasConnection = true
        viewModel.onRefresh()

        verify(mockApiService).getTopFiftyCurrencies(Constants.HEADERS, Constants.API_COUNT, Constants.DEFAULT_SORT)
    }

    @Test
    fun `Error response is handled`(){
        Mockito.`when`(mockApiService.getTopFiftyCurrencies(
            Constants.HEADERS,
            Constants.API_COUNT,
            Constants.DEFAULT_SORT
        )).thenReturn(Single.error(testThrowable))

        viewModel.hasConnection = true

        viewModel.fetchCurrencies()

        await().until(errorIsAdded())

        assert(viewModel.getCurrencyError().value.equals(testThrowable.message))
    }

    private fun currenciesAreAdded(): Callable<Boolean> {
        return object : Callable<Boolean> {
            override fun call(): Boolean {
                return viewModel.getCurrencies().value != null
            }
        }
    }

    private fun errorIsAdded(): Callable<Boolean> {
        return object : Callable<Boolean> {
            override fun call(): Boolean {
                return viewModel.getCurrencyError().value != null
            }
        }
    }
}
