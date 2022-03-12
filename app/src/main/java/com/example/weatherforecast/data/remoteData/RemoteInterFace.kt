package com.example.weatherforecast.data.remoteData

import com.example.weatherforecast.data.model.WeatherModel
import retrofit2.Response
import retrofit2.http.Query

interface RemoteInterFace {
   suspend fun  getWeather( lat: Double,lon:Double) :Response<WeatherModel>
}