package com.sva.weather.repository

import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.model.CityWeatherResponseModel
import com.sva.weather.others.Resource
import kotlinx.coroutines.Deferred

interface CityDataRepository {
    suspend fun getAllCity() : Deferred<List<CityPositionModel>>
}