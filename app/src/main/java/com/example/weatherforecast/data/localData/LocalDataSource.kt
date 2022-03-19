package com.example.weatherforecast.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.WeatherAlert
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val weatherDao: WeatherDao): LocalDataSourceInter {


    override fun getAllWather(): Flow<List<WeatherModel>> {
        return weatherDao.getAll()
    }

    override fun getWeatherByTimeZone(timeZone: String): WeatherModel {
        return weatherDao.getWeatherByTimeZone(timeZone)
    }

    override suspend fun insert(weatherModel: WeatherModel){
        weatherDao.insert(weatherModel)
    }

    override fun deleteByTimeZone(timeZone :String){
        weatherDao.deleteByTimeZone(timeZone)
    }

    override fun getWeatherByLatLong(lat: String, lng: String): WeatherModel {
       return weatherDao.getWeatherByLatLong(lat,lng)
    }

    override fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel {
       return weatherDao.getWeatherByTimeZoneAndNotFav(timezone)
    }

    override fun deleteNotFav() {
        weatherDao.deleteNotFav()
    }

    override fun getAllFavoriteData(): List<WeatherModel> {
       return weatherDao.getAllFavoriteData()
    }

    override fun getAlert(id: Int): WeatherAlert {
        return weatherDao.getAlert(id)
    }

    override suspend fun insertAlert(weatherAlert: WeatherAlert):Long {
      return  weatherDao.insertAlert(weatherAlert)
    }

    override fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return weatherDao.getAllAlerts()
    }

    override suspend fun deleteAlerts(id: Int) {
        weatherDao.deleteAlerts(id)
    }
}