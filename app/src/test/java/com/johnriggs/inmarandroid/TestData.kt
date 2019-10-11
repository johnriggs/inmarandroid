package com.johnriggs.inmarandroid

import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.model.CurrencyMetadata
import com.johnriggsdev.inmarandroid.model.Quote
import com.johnriggsdev.inmarandroid.model.ResponseStatus
import com.johnriggsdev.inmarandroid.model.api.ApiCurrencyResponse
import com.johnriggsdev.inmarandroid.model.api.ApiMetadataResponse

val testQuote = Quote(8226.22342104, 15491365372.5503, 0.0860896,
    -0.368872, -1.3423, 147919425134.2597, "2019-10-08T23:13:33.000Z")

val testCurrency = Currency("1", "Bitcoin", "BTC",
    "bitcoin", "1", "7937", "17981450",
    "17981450", "21000000", "2019-10-08T23:13:33.000Z",
    "2013-04-28T00:00:00.000Z", arrayOf("mineable"), null, mapOf("1" to testQuote))

val testStatus = ResponseStatus("2019-10-08T23:14:45.418Z", 0, null, 7, 1, null)

val testApiCurrencyResponse = ApiCurrencyResponse(testStatus, arrayOf(testCurrency))

val testCurrencyMetadata = CurrencyMetadata(mapOf("website" to arrayOf("https://bitcoin.org/"), "technical_doc" to arrayOf("https://bitcoin.org/bitcoin.pdf")),
    "https://s2.coinmarketcap.com/static/img/coins/64x64/1.png", "1", "Bitcoin", "BTC", "bitcoin",
    "Bitcoin (BTC) is a consensus network that enables a new payment system and a completely digital currency.",
    "2013-04-28T00:00:00.000Z", arrayOf("mineable"), null, "coin")

val testMetadataResponse = ApiMetadataResponse(testStatus, mapOf("1" to testCurrencyMetadata))

val testThrowable = Throwable("This is a test error message")