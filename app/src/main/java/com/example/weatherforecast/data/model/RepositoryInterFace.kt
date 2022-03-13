package com.example.weatherforecast.data.model

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.WeatherModel
import com.example.weatherforecast.data.remoteData.EXCLUDE
import retrofit2.Response
import retrofit2.http.Query

interface RepositoryInterFace {
//    suspend fun getWeather( lat: Double, lon:Double): Response<WeatherModel>
    fun getAllWather(): LiveData<List<WeatherModel>>

    fun getWeatherByTimeZone(timeZone: String): WeatherModel

    suspend fun insert(weatherModel: WeatherModel)

    fun deleteByTimeZone(timeZone :String)

    suspend fun  getWeather(
        lat: String?, lon: String?,
        language: String ="en",
        units:String = "imperial"
                             ):WeatherModel

}