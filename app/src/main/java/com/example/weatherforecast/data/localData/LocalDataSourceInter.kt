package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.model.WeatherAlert
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInter {
    fun getAllWather(): Flow<List<WeatherModel>>

    fun getWeatherByTimeZone(timeZone: String): WeatherModel

    suspend fun insert(weatherModel: WeatherModel)

    fun deleteByTimeZone(timeZone :String)

    fun getWeatherByLatLong(lat: String, lng: String):WeatherModel
    fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel
    fun deleteNotFav()
    fun getAllFavoriteData(): List<WeatherModel>


    suspend fun  insertAlert(weatherAlert: WeatherAlert):Long
    fun getAllAlerts():Flow<List<WeatherAlert>>
    suspend fun deleteAlerts(id:Int)
    fun getAlert(id:Int):WeatherAlert


}
