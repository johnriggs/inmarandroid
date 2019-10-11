package com.johnriggsdev.inmarandroid.utils

@Suppress("unused")
class Constants {
    companion object {
        const val APP_TAG = "Inmar App"
        private const val HEADER_KEY = "X-CMC_PRO_API_KEY"
        private const val API_KEY = "8c796f75-56ce-46da-88b3-aac599bcb07c"
        private const val APP_JSON = "application/json"
        private const val ACCEPT_KEY = "Accept"
        const val API_COUNT = "50"
        const val BASE_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/"
        const val LISTINGS_LATEST_URL = "listings/latest"
        const val METADATA_URL = "info"
        const val DEFAULT_SORT = "market_cap"

        const val PREFS_SORT_KEY = "prefsSortKey"
        const val SORT_MKC = "market_cap"
        const val SORT_NAME = "name"
        const val SORT_PRICE = "price"
        const val SORT_DATE = "date_added"
        const val SORT_24H = "volume_24h"

        val HEADERS: MutableMap<String, String> = mutableMapOf(ACCEPT_KEY to APP_JSON, HEADER_KEY to API_KEY)
    }
}