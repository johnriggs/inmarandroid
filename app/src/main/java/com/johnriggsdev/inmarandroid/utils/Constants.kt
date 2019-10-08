package com.johnriggsdev.inmarandroid.utils

class Constants {
    companion object {
        const val APP_TAG = "Inmar App"
        const val HEADER_KEY = "X-CMC_PRO_API_KEY"
        private const val API_KEY = "8c796f75-56ce-46da-88b3-aac599bcb07c"
        const val APP_JSON = "application/json"
        const val ACCEPT_KEY = "Accept"
//        const val CONTENT_TYPE_KEY = "Content-type"
        const val API_COUNT = 50
        const val DEFAULT_API_START = 1
        const val BASE_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/"
        const val LISTINGS_LATEST_URL = "listings/latest"
        const val METADATA_URL = "info"

        fun getApiKey() : String = API_KEY
    }
}