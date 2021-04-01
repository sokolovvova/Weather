package com.sva.weather.extensions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sva.weather.model.*
import java.text.SimpleDateFormat

fun <T:Any?> MutableLiveData<T>.default(initValue: T) = apply { value = initValue }
fun <T> MutableLiveData<T>.set(newValue: T) = apply { value = newValue }
fun <T> MutableLiveData<T>.post(newValue: T) = apply { this.postValue(newValue) }

fun CityWeatherResponseModel.toCityWeatherModel() : CityWeatherModel {
    val temp = if(this.current!=null){
        this.current.temp.toCelsius()
    }
    else ""

    val icon = if(this.current?.weather !=null){
        this.current.weather[0].icon.toString()
    }
    else ""

    val currentTime = if(this.current?.dt!=null){
        this.current.dt
    }
    else 0

    val tzOffset = this.timezone_offset ?: 0

    var dailyWeather = arrayListOf<DailyWeatherModel>()
    for(day in this.daily!!){
        dailyWeather.add(day.toDailyWeatherModel())
    }

    return CityWeatherModel(icon = icon,
    temp = temp,
    city = CityPositionModel(),
    currentTime = currentTime,
    timezoneOffset = tzOffset,
    daily = dailyWeather)
}

fun Daily.toDailyWeatherModel(): DailyWeatherModel{
    return DailyWeatherModel(
        time = this.dt!!,
        temp = this.temp?.day!!,
        img = this.weather?.get(0)?.icon!!,
        description = this.weather[0].description!!,
        wingSpeed = this.wind_speed!!,
        pressure = this.pressure!!
    )
}

fun CityWeatherModel.toSavedCityModel() : SavedCityModel{
    val gson = Gson()
    val dailyString = gson.toJson(this.daily)
    val cityString = gson.toJson(this.city)
    return SavedCityModel(
        icon = this.icon,
        cityId = this.city.id,
        city = cityString,
        temp = this.temp,
        currentTime = this.currentTime,
        timezoneOffset = this.timezoneOffset,
        daily = dailyString
    )
}

fun SavedCityModel.toCityWeatherModel() : CityWeatherModel{
    val gson = Gson()
    val listType = object :  TypeToken<ArrayList<DailyWeatherModel>>(){}.type
    val dailyData : ArrayList<DailyWeatherModel> = gson.fromJson(this.daily,listType)
    val cityObject = gson.fromJson(this.city,CityPositionModel::class.java)

    return CityWeatherModel(
        icon = this.icon!!,
        city = cityObject,
        temp = this.temp!!,
        currentTime = this.currentTime!!,
        timezoneOffset = this.timezoneOffset!!,
        daily = dailyData
    )
}

fun DailyWeatherModel.toDailyWeatherConvertedModel(context: Context) : DailyWeatherConvertedModel{

    fun dateCorrector(date: Long): String{
        val sdf = SimpleDateFormat("dd.MM")
        return sdf.format(date*1000)
    }
    fun imgGetter(img: String) : Drawable{
        val ims = context.assets.open("weather_icons/${img}.png")
        return Drawable.createFromStream(ims,null)
    }

    return DailyWeatherConvertedModel(
        time = dateCorrector(this.time),
        temp = this.temp.toCelsius(),
        img = imgGetter(this.img),
        description = this.description,
        wingSpeed = "wing speed: ${this.wingSpeed} m/s",
        pressure = "pressure: ${this.pressure} mm Hg"
    )
}


fun Float?.toCelsius() :String{
    return if(this!=null){
        var cel = (this-273.15).toInt()
        if(cel>0) {
            "+"+ cel.toString()+"C"
        }
        else cel.toString()+"C"
    }
    else ""
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}