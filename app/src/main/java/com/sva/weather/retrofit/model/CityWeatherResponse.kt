package com.sva.weather.model

data class CityWeatherResponseModel (
    val timezone_offset: Int? = null,
    val current : Current? =null,
    val daily : ArrayList<Daily>? =null
)

data class Weather(
    val icon: String? = null,
    val description: String? = null
)
data class Current(
    val temp: Float? = null,
    val weather : ArrayList<Weather>? = null,
    val dt: Long? = null,
)

data class Daily(
    val dt: Long? = null,
    val temp: Temp? =null,
    val weather : ArrayList<Weather>? = null,
    val wind_speed: Float? =null,
    val pressure: Int? =null,
)

data class Temp(
    val day: Float? = null,
)