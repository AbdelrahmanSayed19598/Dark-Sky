package com.example.weatherforecast.data.remoteData

import com.example.weatherforecast.data.model.WeatherModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper: RemoteInterFace {
    private const val baseURL = "https://api.openweathermap.org/data/2.5/"
fun getInstance(): Retrofit{
return Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
}

    override suspend fun getWeather(
        lat: String?,
        lon: String?,
        language: String,
        units: String
    ): Response<WeatherModel> {
        return  getInstance().create(ApiWeatherService::class.java).getWeather(lat,lon,language,units)

    }


}