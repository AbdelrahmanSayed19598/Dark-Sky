package com.example.weatherforecast.data.model

import com.example.weatherforecast.data.remoteData.RemoteInterFace

class Repository(private val remote : RemoteInterFace) : RepositoryInterFace {
    override suspend fun getWeather(lat: Double, lon: Double) = remote.getWeather(lat,lon)
}