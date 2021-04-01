package com.sva.weather.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeatherModel(
    val time: Long,
    val temp: Float,
    val img: String,
    val description: String,
    val wingSpeed: Float,
    val pressure: Int
) : Parcelable