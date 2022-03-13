package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.WeatherModel

interface LocalDataSourceInter {
    fun getAllWather(): LiveData<List<WeatherModel>>

    fun getWeatherByLatLon(timeZone: String): WeatherModel

    suspend fun insert(weatherModel: WeatherModel)

    fun deleteByTimeZone(timeZone :String)
}