package com.johnriggsdev.inmarandroid

import android.util.Log
import com.johnriggsdev.inmarandroid.main.MainActivity
import com.johnriggsdev.inmarandroid.main.MainViewModel
import com.johnriggsdev.inmarandroid.model.api.CryptoCurrencyService
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class MainMVVMTest {

    lateinit var mockApiService : CryptoCurrencyService
    lateinit var mockView : MainActivity
    lateinit var viewModel : MainViewModel

    @Before
    fun setup(){
//        PowerMockito.mockStatic(Log::class.java)

        mockApiService = mock()
        mockView = mock()
        viewModel = MainViewModel()
    }

    @Test
    fun `updateConnection sets boolean`() {
        viewModel.updateConnection(false)
        assert(!viewModel.hasConnection)
        assert(!viewModel.getIsProgressing().value!!)

        viewModel.updateConnection(true)
        assert(!viewModel.hasConnection)
    }
}
