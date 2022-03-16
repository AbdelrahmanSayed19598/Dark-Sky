package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInter {
    fun getAllWather(): Flow<List<WeatherModel>>

    fun getWeatherByLatLon(timeZone: String): WeatherModel

    suspend fun insert(weatherModel: WeatherModel)

    fun deleteByTimeZone(timeZone :String)


    fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel
    fun deleteNotFav()
}