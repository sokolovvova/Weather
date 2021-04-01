package com.sva.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sva.weather.extensions.default
import com.sva.weather.extensions.post
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.repository.DbSavedDataRep
import com.sva.weather.repository.SavedDataRepository
import kotlinx.coroutines.*

class DailyWeatherViewModel : ViewModel() {

    private var _weatherData = MutableLiveData<CityWeatherModel>()
    val weatherData: LiveData<CityWeatherModel> = _weatherData
    private var _savedWeatherData = MutableLiveData<List<CityWeatherModel>>()
    val savedWeatherData: LiveData<List<CityWeatherModel>> = _savedWeatherData

    private val _saved = MutableLiveData<Boolean>().default(false)
    val saved: LiveData<Boolean> = _saved

    private var savedDataRep: SavedDataRepository = DbSavedDataRep()

    fun updateWeatherData(data: CityWeatherModel) {
        _weatherData.post(data)
    }

    fun updateSavedWeatherData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = savedDataRep.getAllSavedCities().await()
            _savedWeatherData.postValue(data)
        }
    }

    private fun saveWeatherCity() {
        CoroutineScope(Dispatchers.IO).launch {
            savedDataRep.insertCity(weatherData.value!!)
            delay(250L)
            updateSavedWeatherData()
        }
    }

    private fun deleteSavedCity() {
        CoroutineScope(Dispatchers.IO).launch {
        savedDataRep.deleteSavedCityById(weatherData.value!!.city.id)
            delay(250L)
        updateSavedWeatherData()}
    }

    fun saveClickProcessing() {
        _saved.postValue(!saved.value!!)
        if (saved.value!!) deleteSavedCity()
        else saveWeatherCity()
    }

    fun checkSaved() {
        if (weatherData.value != null && savedWeatherData.value != null) {
            val itemId = weatherData.value!!.city.id
            for (saved in savedWeatherData.value!!) {
                if (saved.city.id == itemId) {
                    _saved.postValue(true)
                    break
                }
            }
        }
    }
}