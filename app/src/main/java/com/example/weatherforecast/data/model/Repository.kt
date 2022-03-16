package com.example.weatherforecast.data.model

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.localData.LocalDataSource
import com.example.weatherforecast.data.localData.LocalDataSourceInter
import com.example.weatherforecast.data.localData.WeatherDataBase
import com.example.weatherforecast.data.remoteData.RemoteInterFace
import com.example.weatherforecast.data.remoteData.RetrofitHelper
import com.example.weatherforecast.lat
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class Repository(private val remote : RemoteInterFace,private val local : LocalDataSourceInter) : RepositoryInterFace {

    companion object {
        lateinit var latitude: String
        lateinit var longitude: String
        lateinit var sharedPref: SharedPreferences
        lateinit var map :String
        @Volatile
        private var INSTANCE: Repository? = null
        fun getRepoInstance(application: Application): Repository {
            sharedPref = application.getSharedPreferences(lat, Context.MODE_PRIVATE)
            latitude = sharedPref.getString("lat", "0").toString()
            longitude = sharedPref.getString("lon", "0").toString()
            map = sharedPref.getString("map","0").toString()

            return INSTANCE ?: synchronized(this) {
                Repository(RetrofitHelper,
                    LocalDataSource(WeatherDataBase.getDataBase(application).weatherDao())

                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    override fun getAllWather(): Flow<List<WeatherModel>> {
        return local.getAllWather()
    }

    override fun getWeatherByTimeZone(timeZone: String): WeatherModel {

      return  local.getWeatherByLatLon(timeZone)
    }

    override suspend fun insert(weatherModel: WeatherModel) {
        local.insert(weatherModel)
    }

    override fun deleteByTimeZone(timeZone: String) {
        local.deleteByTimeZone(timeZone)
    }

    override fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel {
       return local.getWeatherByTimeZoneAndNotFav(timezone)
    }

    override fun deleteNotFav() {
        local.deleteNotFav()
    }

    override suspend fun getWeather(
        lat: String?,
        lon: String?,
        language: String,
        units: String
    ): WeatherModel {
        val response = remote.getWeather(lat,lon,language,units)
        if (response.isSuccessful){
            if ((latitude.toDouble() == 0.0 && longitude.toDouble() == 0.0) || map.toInt()==0){
                deleteNotFav()
                insert(response.body()!!)
            }

            return response.body()!!
        }else{
            throw Exception("${response.errorBody()}")
        }


    }

    override suspend fun insertFavoriteWeather(
        lat: String?,
        lon: String?,
        language: String,
        units: String
    ): WeatherModel {
        val response = remote.getWeather(lat,lon,language,units)
        if (response.isSuccessful){
            response.body()?.isFav =1
            local.insert(response.body()!!)
            return response.body()!!
        }else{
            throw Exception("${response.errorBody()}")
        }
    }


}