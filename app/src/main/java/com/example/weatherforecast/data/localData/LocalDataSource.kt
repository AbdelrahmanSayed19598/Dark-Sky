package com.example.weatherforecast.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val weatherDao: WeatherDao): LocalDataSourceInter {


    override fun getAllWather(): Flow<List<WeatherModel>> {
        return weatherDao.getAll()
    }

    override fun getWeatherByLatLon(timeZone: String): WeatherModel {
        return weatherDao.getWeatherByTimeZone(timeZone)
    }

    override suspend fun insert(weatherModel: WeatherModel){
        weatherDao.insert(weatherModel)
    }

    override fun deleteByTimeZone(timeZone :String){
        weatherDao.deleteByTimeZone(timeZone)
    }

    override fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel {
       return weatherDao.getWeatherByTimeZoneAndNotFav(timezone)
    }

    override fun deleteNotFav() {
        weatherDao.deleteNotFav()
    }


}