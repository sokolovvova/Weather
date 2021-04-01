package com.sva.weather.retrofit.api

import com.sva.weather.model.CityWeatherResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {

    @GET("onecall")
    suspend fun getSimpleWeather(
        @Query ("lat") lat: String,
        @Query ("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String
    ): Response<CityWeatherResponseModel>
}