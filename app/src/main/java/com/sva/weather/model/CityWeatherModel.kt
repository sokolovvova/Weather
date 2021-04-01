package com.sva.weather.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityWeatherModel (
    val icon: String,
    var city: CityPositionModel,
    val temp: String,
    val currentTime: Long,
    val timezoneOffset: Int,
    val daily: ArrayList<DailyWeatherModel>
    ) : Parcelable