package com.sva.weather.repository

import android.util.Log
import com.sva.weather.extensions.toCityWeatherModel
import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.CityWeatherModel
import com.sva.weather.others.Resource
import com.sva.weather.others.Status
import com.sva.weather.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class NetworkWeatherDataRepository: WeatherDataRepository {

    override suspend fun getWeatherData(city: CityPositionModel): Deferred<Resource<CityWeatherModel>> {
        return CoroutineScope(Dispatchers.IO).async {
            var result: Resource<CityWeatherModel>
            try{
                val response = RetrofitClient.client.getSimpleWeather(lat = city.lat.toString(),lon = city.lng.toString(),apiKey = "0516b70d5f66a33f99764a5e0938b64f", exclude = "minutely,hourly,alerts")
                if(response.isSuccessful) {

                    val converted = response.body()!!.toCityWeatherModel()
                    converted.city = city
                    result = Resource.success(converted)
                }
                else result = Resource.failed(response.code().toString())
            }
            catch (e: Throwable){
                result = Resource.error(e.toString())
            }
            result
        }
    }

    override suspend fun getMultipleWeatherData(cityList: ArrayList<CityPositionModel>): Deferred<Resource<ArrayList<CityWeatherModel>>> {
        return CoroutineScope(Dispatchers.IO).async {
            var sucesses =0
            var failed = 0
            val data = arrayListOf<CityWeatherModel>()
            var result : Resource<ArrayList<CityWeatherModel>>
            var lastError=""
            var lastFailed=""
            for(city in cityList){
                val cityRes = getWeatherData(city).await()
                when(cityRes.status){
                    Status.SUCCESS->{
                        data.add(cityRes.data!!)
                        sucesses++
                    }
                    Status.FAILED->{
                        lastFailed=cityRes.message.toString()
                        failed++
                    }
                    Status.ERROR->{
                        lastError=cityRes.message.toString()
                        break
                    }
                    else -> {
                    }
                }
            }
            Log.d("test","$sucesses  $failed")
            if(sucesses>0&&failed>0){
                result = Resource.successWithErrors(data,"errors: $failed")
            }
            else if(sucesses>0) result = Resource.success(data)
            else if(failed>0) result = Resource.failed(lastFailed)
            else result = Resource.error(lastError)
            result
        }
    }
}