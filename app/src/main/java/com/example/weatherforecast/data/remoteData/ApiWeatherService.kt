package com.example.weatherforecast.data.remoteData

import com.example.weatherforecast.data.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY ="dff36c85b0831f68305233964550b263"
const val EXCLUDE ="minutely"
//https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=minutely&appid=dff36c85b0831f68305233964550b263
interface ApiWeatherService {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("lang") language: String ="en",
        @Query("units") units:String = "imperial",
        @Query("exclude") exclude: String = EXCLUDE,
        @Query("appid") appid: String = API_KEY
    ):Response<WeatherModel>

}