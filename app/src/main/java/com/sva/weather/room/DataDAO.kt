package com.sva.weather.room

import androidx.room.*
import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.SavedCityModel

@Dao
interface DataDAO {
    @Query("SELECT * FROM city_coords")
    fun getAllCity(): List<CityPositionModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCity(data: SavedCityModel)
    @Query("DELETE from saved_city where city_id = :id")
    fun deleteSavedCityById(id: Int)
    @Query("SELECT * FROM saved_city")
    fun getAllSavedCities() : List<SavedCityModel>
}