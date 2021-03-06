package com.example.weatherforecast.data.model

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.WeatherModel
import com.example.weatherforecast.data.remoteData.EXCLUDE
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Query

interface RepositoryInterFace {
//    suspend fun getWeather( lat: Double, lon:Double): Response<WeatherModel>
    fun getAllWather(): Flow<List<WeatherModel>>

    fun getWeatherByTimeZone(timeZone: String): WeatherModel

    suspend fun insert(weatherModel: WeatherModel)

    fun deleteByTimeZone(timeZone :String)

    fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel
    fun deleteNotFav()

    suspend fun  getWeather(
        lat: String?, lon: String?,
        language: String ,
        units:String
                             ):WeatherModel

    suspend fun  insertFavoriteWeather(
        lat: String?, lon: String?,
        language: String ,
        units:String
    ):WeatherModel

    fun refrechData()
    fun getWeatherByLatLong(lat: String, lng: String): WeatherModel

    suspend fun  insertAlert(weatherAlert: WeatherAlert):Long
    fun getAllAlerts():Flow<List<WeatherAlert>>
    suspend fun deleteAlerts(id:Int)
    fun getAlert(id:Int):WeatherAlert

}