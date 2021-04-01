package com.sva.weather.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName="city_coords")
data class CityPositionModel (
    @PrimaryKey
    val id: Int = 0,
    var city: String? = "",
    var country: String? = "",
    val lat: Float? = 0f,
    val lng: Float? = 0f
    ) : Parcelable