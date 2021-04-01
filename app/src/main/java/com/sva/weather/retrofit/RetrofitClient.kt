package com.sva.weather.retrofit

import com.sva.weather.retrofit.api.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val MainServer ="https://api.openweathermap.org/data/2.5/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(MainServer)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val client: WeatherApiService = retrofit.create(WeatherApiService::class.java)

}