package dev.android.atmosphere.data.util


object ErrorMessages {

    const val NO_NETWORK = "Нет подключения к интернету"
    const val NO_NETWORK_FOR_UPDATE = "Нет подключения к интернету для обновления данных"
    const val NETWORK_ERROR = "Ошибка сети: %s"
    const val HTTP_ERROR = "HTTP ошибка"
    const val UNKNOWN_ERROR = "Неизвестная ошибка: %s"
    const val NO_CACHED_WEATHER = "Нет кэшированных данных о погоде"
    const val NO_CACHED_FORECAST = "Нет кэшированных данных о прогнозе"
    const val CACHE_ERROR = "Ошибка при получении кэшированных данных: %s"
    const val NO_CACHED_CITY_DATA = "Нет подключения к интернету и нет кэшированных данных для города %s"


    fun formatNetworkError(message: String?) = NETWORK_ERROR.format(message)
    fun formatUnknownError(message: String?) = UNKNOWN_ERROR.format(message)
    fun formatCacheError(message: String?) = CACHE_ERROR.format(message)
    fun formatNoCityData(cityName: String) = NO_CACHED_CITY_DATA.format(cityName)
}