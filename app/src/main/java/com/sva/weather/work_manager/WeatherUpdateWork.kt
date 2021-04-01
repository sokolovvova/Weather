package com.sva.weather.work_manager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.others.Status
import com.sva.weather.repository.DbSavedDataRep
import com.sva.weather.repository.NetworkWeatherDataRepository
import com.sva.weather.repository.SavedDataRepository
import com.sva.weather.repository.WeatherDataRepository
import kotlinx.coroutines.*

class WeatherUpdateWork(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private var weatherDataRep: WeatherDataRepository = NetworkWeatherDataRepository()
    private var savedDataRep: SavedDataRepository = DbSavedDataRep()

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val data = savedDataRep.getAllSavedCities().await()
            val cityList = arrayListOf<CityPositionModel>()
            for (item in data) cityList.add(item.city)
            var weatherList = arrayListOf<CityWeatherModel>()
            val result = weatherDataRep.getMultipleWeatherData(cityList).await()
            when (result.status) {
                Status.SUCCESS -> {
                    weatherList = result.data!!
                }
                Status.ERROR -> {
                }
                Status.FAILED -> {
                }
                Status.SUCCESS_WITH_ERRORS -> {
                    weatherList = result.data!!
                }
            }
            if (weatherList.size > 0) {
                for (weather in weatherList) {
                    savedDataRep.deleteSavedCityById(weather.city.id)
                    delay(150L)
                    savedDataRep.insertCity(weather)
                    delay(150L)
                }
            }
            else return@withContext Result.failure()
            return@withContext Result.success()
        }
    }
}