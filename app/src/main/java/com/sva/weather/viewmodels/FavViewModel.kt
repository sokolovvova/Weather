package com.sva.weather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.repository.DbSavedDataRep
import com.sva.weather.repository.SavedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel : ViewModel() {

    private var _savedWeatherData = MutableLiveData<List<CityWeatherModel>>()
    val savedWeatherData: LiveData<List<CityWeatherModel>> = _savedWeatherData

    private var savedDataRep: SavedDataRepository = DbSavedDataRep()

    fun updateSavedWeatherData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = savedDataRep.getAllSavedCities().await()
            _savedWeatherData.postValue(data)
        }
    }
}