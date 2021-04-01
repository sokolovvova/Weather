package com.sva.weather.repository

import com.sva.weather.model.CityPositionModel
import com.sva.weather.room.DataDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class DbCityDataRepository: CityDataRepository {
    override suspend fun getAllCity(): Deferred<List<CityPositionModel>> {
        return CoroutineScope(Dispatchers.IO).async {
            val dataDB = DataDB.getDataDB()
            val data = dataDB.dataDAO().getAllCity()
            data
        }
    }
}