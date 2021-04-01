package com.sva.weather.others

import com.sva.weather.model.CityWeatherModel

sealed class WeatherState{
    class DefaultState: WeatherState()
    class StartLoadingState: WeatherState()
    class DataGetSuccesState(val data: ArrayList<CityWeatherModel>): WeatherState()
}