package com.sva.weather.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sva.weather.model.CityPositionModel
import com.sva.weather.model.SavedCityModel

@Database(
    entities = [CityPositionModel::class,SavedCityModel::class],
    version = 1,
    exportSchema = false
)
abstract class DataDB : RoomDatabase() {
    abstract fun dataDAO(): DataDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: DataDB

        fun init(context: Context) {
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, DataDB::class.java, "LDD_DATABASE")
                    .createFromAsset("database/app_db.db").build()
            }
        }

        fun getDataDB(): DataDB {
            return INSTANCE
        }
    }
}