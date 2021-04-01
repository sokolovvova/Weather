package com.sva.weather.repository

import com.sva.weather.model.CityWeatherModel
import kotlinx.coroutines.Deferred

interface SavedDataRepository {
    fun deleteSavedCityById(id: Int)
    fun insertCity(data: CityWeatherModel)
    fun getAllSavedCities(): Deferred<List<CityWeatherModel>>
}