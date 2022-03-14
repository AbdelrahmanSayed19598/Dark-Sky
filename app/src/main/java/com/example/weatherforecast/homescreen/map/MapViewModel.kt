package com.example.weatherforecast.homescreen.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val iRepo: RepositoryInterFace) : ViewModel() {
    lateinit var weatherModel: WeatherModel

    fun insertData(
        lat: String?,
        lon: String?,
        language: String = "en",
        units: String = "imperial"
    ) {


        var job = viewModelScope.launch(Dispatchers.IO) {

            weatherModel = iRepo.getWeather(lat, lon, language, units)
            iRepo.deleteNotFav()
        }
        viewModelScope.launch(Dispatchers.IO) {
            job.join()
            var response = iRepo.insert(weatherModel)

        }
    }
}