package com.example.weatherforecast.ui.map.viewModel

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
        language: String ,
        units: String
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

    fun insertFavoritePlace(
        lat: String?,
        lon: String?,
        language: String ,
        units: String
    ) {
        var job = viewModelScope.launch(Dispatchers.IO) {
             iRepo.insertFavoriteWeather(lat, lon, language, units)
        }


    }
}