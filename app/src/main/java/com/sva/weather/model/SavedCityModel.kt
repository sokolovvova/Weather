package com.sva.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="saved_city")
data class SavedCityModel (
    val icon: String?,
    val city: String?,
    @PrimaryKey
    @ColumnInfo(name="city_id")
    var cityId: Int,
    val temp: String?,
    @ColumnInfo(name="current_time")
    val currentTime: Long?,
    @ColumnInfo(name="tz_offset")
    val timezoneOffset: Int?,
    val daily: String?
    )