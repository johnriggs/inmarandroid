package com.johnriggs.inmarandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.johnriggsdev.inmarandroid.details.DetailsViewModel
import com.johnriggsdev.inmarandroid.model.api.CryptoCurrencyService
import com.johnriggsdev.inmarandroid.utils.Constants
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.HEADERS
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.awaitility.Awaitility.await
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.Callable

class DetailsFragmentTest {
    @get:Rule
    val testRule = InstantTaskExecutorRule()
    companion object {
        @get:ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    lateinit var mockApiService : CryptoCurrencyService
    lateinit var viewModel : DetailsViewModel

    @Before
    fun setup(){
        mockApiService = mock()
        viewModel = DetailsViewModel()

        viewModel.service = mockApiService
        viewModel.currency = testCurrency
    }

    @Test
    fun `initializing sets currency`(){
        Mockito.`when`(mockApiService.getMetadataForIDs(
            Constants.HEADERS,
            viewModel.currency.id
        )).thenReturn(Single.just(testMetadataResponse))

        viewModel.initialize(testCurrency)

        assert(viewModel.currency == testCurrency)
    }

    @Test
    fun `fetchMetadata get currency metadata`(){
        Mockito.`when`(mockApiService.getMetadataForIDs(
            Constants.HEADERS,
            viewModel.currency.id
        )).thenReturn(Single.just(testMetadataResponse))

        viewModel.fetchMetadata(testCurrency)

        verify(mockApiService).getMetadataForIDs(HEADERS, viewModel.currency.id)

        await().until(metadataIsAdded())

        assert(viewModel.getCurrencyMetadata().value == testMetadataResponse.data.getValue(viewModel.currency.id))
    }

    @Test
    fun `Error response is handled`(){
        Mockito.`when`(mockApiService.getMetadataForIDs(
            Constants.HEADERS,
            viewModel.currency.id
        )).thenReturn(Single.error(testThrowable))

        viewModel.fetchMetadata(testCurrency)

        await().until(errorIsAdded())

        assert(viewModel.getMetadataError().value.equals(testThrowable.message))
    }

    private fun metadataIsAdded(): Callable<Boolean> {
        return object : Callable<Boolean>{
            override fun call(): Boolean {
                return viewModel.getCurrencyMetadata().value != null
            }
        }
    }

    private fun errorIsAdded(): Callable<Boolean> {
        return object : Callable<Boolean>{
            override fun call(): Boolean {
                return viewModel.getMetadataError().value != null
            }
        }
    }
}