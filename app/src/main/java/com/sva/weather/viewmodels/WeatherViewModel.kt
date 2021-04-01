package com.sva.weather.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sva.weather.extensions.default
import com.sva.weather.extensions.post
import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.others.Status
import com.sva.weather.others.WeatherState
import com.sva.weather.repository.CityDataRepository
import com.sva.weather.repository.DbCityDataRepository
import com.sva.weather.repository.WeatherDataRepository
import com.sva.weather.repository.NetworkWeatherDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private var weatherDataRep: WeatherDataRepository = NetworkWeatherDataRepository()
    private var cityDataRep: CityDataRepository = DbCityDataRepository()
    val state = MutableLiveData<WeatherState>().default(initValue = WeatherState.DefaultState())

    private val _cityData = MutableLiveData<List<CityPositionModel>>()
    val cityData: LiveData<List<CityPositionModel>> = _cityData

    private fun getWeatherData(cityList: ArrayList<CityPositionModel>) {
        state.post(WeatherState.StartLoadingState())
        CoroutineScope(Dispatchers.IO).launch {
            var weatherList = arrayListOf<CityWeatherModel>()
            val result = weatherDataRep.getMultipleWeatherData(cityList).await()
            when (result.status) {
                Status.SUCCESS -> {
                    Log.d("test", "SUCCESS ${result.data}")
                    weatherList = result.data!!
                }
                Status.ERROR -> {
                    Log.d("test", "ERROR ${result.message}")
                }
                Status.FAILED -> {
                    Log.d("test", "FAILED ${result.message}")
                }
                Status.SUCCESS_WITH_ERRORS -> {
                    weatherList = result.data!!
                    Log.d("test", "SUCCESS_WITH_ERRORS ${result.message}")
                }
            }
            state.post(WeatherState.DataGetSuccesState(weatherList))
        }
    }

    fun getWeatherDataOfClickedItem(pos: Int) {
        val cityData = arrayListOf(cityData.value?.get(pos)!!)
        getWeatherData(cityData)
    }

    fun getAllCityData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = cityDataRep.getAllCity().await()
            Log.d("test", "cities: ${data.size}")
            _cityData.postValue(data)
        }
    }

    fun getSearchedCityWeather(searchString: String) {
        state.post(WeatherState.StartLoadingState())
        val resultCities = arrayListOf<CityPositionModel>()
        for (city in cityData.value!!) {
            val cityString = city.city!! + " (" + city.country + ")"
            if (cityString.toUpperCase().startsWith(searchString.toUpperCase())) resultCities.add(
                city
            )
        }
        getWeatherData(ArrayList(resultCities.take(10)))
    }

    fun getNearestCitiesWeather(location: Location) {
        state.post(WeatherState.StartLoadingState())

        val lat = location.latitude
        val lng = location.longitude

        val cityDistanceList = arrayListOf<Int>()
        for (city in cityData.value!!) {
            val result: FloatArray = FloatArray(1)
            Location.distanceBetween(lat, lng, city.lat!!.toDouble(), city.lng!!.toDouble(), result)
            cityDistanceList.add(result[0].toInt())
        }
        var nearestCitiesList = ArrayList<Int>(cityDistanceList)
        nearestCitiesList = nearestCitiesList.distinct() as ArrayList<Int>
        nearestCitiesList.sort()
        nearestCitiesList = nearestCitiesList.take(5) as ArrayList<Int>
        val resultCities = arrayListOf<CityPositionModel>()
        for (x in 0..4) {
            resultCities.add(cityData.value!![cityDistanceList.indexOf(nearestCitiesList[x])])
        }

        getWeatherData(resultCities)
    }
}