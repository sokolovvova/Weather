package com.sva.weather.repository

import com.sva.weather.extensions.toCityWeatherModel
import com.sva.weather.extensions.toSavedCityModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.room.DataDB
import kotlinx.coroutines.*

class DbSavedDataRep: SavedDataRepository {
    override fun deleteSavedCityById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataDB = DataDB.getDataDB()
            dataDB.dataDAO().deleteSavedCityById(id)
        }
    }

    override fun insertCity(data: CityWeatherModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataDB = DataDB.getDataDB()
            dataDB.dataDAO().saveCity(data.toSavedCityModel())
        }
    }

    override fun getAllSavedCities(): Deferred<List<CityWeatherModel>> {
        return CoroutineScope(Dispatchers.IO).async {
            val dataDB = DataDB.getDataDB()
            val data = dataDB.dataDAO().getAllSavedCities()
            val convertedData = arrayListOf<CityWeatherModel>()
            for(city in data){
                convertedData.add(city.toCityWeatherModel())
            }
            convertedData
        }
    }
}