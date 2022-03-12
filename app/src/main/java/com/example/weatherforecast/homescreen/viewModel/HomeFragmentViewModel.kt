package com.example.weatherforecast.homescreen.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.data.model.Current
import com.example.weatherforecast.data.model.Daily
import com.example.weatherforecast.data.model.Hourly
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val iRepo: RepositoryInterFace) : ViewModel() {
    val allWeatherList = MutableLiveData<WeatherModel>()
    val currentWeatherList = MutableLiveData<Current>()
    val dailyWeatherList = MutableLiveData<List<Daily>>()
    val hourlyWeatherList = MutableLiveData<List<Hourly>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllWeather(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = iRepo.getWeather(lat, lon)
            if (response.isSuccessful) {
                allWeatherList.postValue(response.body())
            } else {
                errorMessage.postValue(response.message())
            }
        }

    }

    suspend fun getCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = iRepo.getWeather(lat, lon)
            if (response.isSuccessful) {
                currentWeatherList.postValue(response.body()?.current)
            } else {
                errorMessage.postValue(response.message())
            }
        }
    }

    suspend fun getHourlyWeather(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = iRepo.getWeather(lat, lon)
            if (response.isSuccessful) {
                hourlyWeatherList.postValue(response.body()?.hourly ?: emptyList())
            } else {
                errorMessage.postValue(response.message())
            }
        }
    }

    suspend fun getDailyWeather(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = iRepo.getWeather(lat, lon)
            if (response.isSuccessful) {
                dailyWeatherList.postValue(response.body()?.daily ?: emptyList())
            } else {
                errorMessage.postValue(response.message())
            }
        }
    }
}