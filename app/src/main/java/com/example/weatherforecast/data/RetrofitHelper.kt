package com.example.weatherforecast.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val baseURL = "https://api.openweathermap.org/data/2.5/"
fun getInstance(): Retrofit{
return Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
}
}