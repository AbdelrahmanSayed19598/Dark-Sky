package com.example.weatherforecast.data.model

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.localData.LocalDataSource
import com.example.weatherforecast.data.localData.LocalDataSourceInter
import com.example.weatherforecast.data.localData.WeatherDataBase
import com.example.weatherforecast.data.remoteData.RemoteInterFace
import com.example.weatherforecast.data.remoteData.RetrofitHelper
import java.lang.Exception

class Repository(private val remote : RemoteInterFace,private val local : LocalDataSourceInter) : RepositoryInterFace {

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getRepoInstance(application: Application): Repository {
            return INSTANCE ?: synchronized(this) {
                Repository(RetrofitHelper,
                    LocalDataSource(WeatherDataBase.getDataBase(application).weatherDao())

                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    override fun getAllWather(): LiveData<List<WeatherModel>> {
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

    override suspend fun getWeather(
        lat: String?,
        lon: String?,
        language: String,
        units: String
    ): WeatherModel {
        val response = remote.getWeather(lat,lon,language,units)
        if (response.isSuccessful){
            return response.body()!!
        }else{
            throw Exception("${response.errorBody()}")
        }


    }


}