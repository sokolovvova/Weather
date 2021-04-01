package com.sva.weather.model

import android.graphics.drawable.Drawable


data class DailyWeatherConvertedModel(
    val time: String,
    val temp: String,
    val img: Drawable,
    val description: String,
    val wingSpeed: String,
    val pressure: String
)