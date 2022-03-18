package com.example.weatherforecast.ui.home.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.data.model.WeatherModel
import com.example.weatherforecast.ui.activity.timeZoneShared
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val iRepo: RepositoryInterFace, private val context: Context) :
    ViewModel() {

    private var _allWeather = MutableLiveData<WeatherModel>()
    val allWeather: LiveData<WeatherModel> = _allWeather
    lateinit var sharedPref: SharedPreferences
    lateinit var timezone: String

    fun getData(
        lat: String?,
        lon: String?,
        language: String,
        units: String
    ) {

        sharedPref =
            context.getSharedPreferences(com.example.weatherforecast.ui.activity.lat, Context.MODE_PRIVATE)
        timezone = sharedPref.getString(timeZoneShared, "null").toString()

        if (isNetworkAvailable(context)) {
            val job = viewModelScope.launch(Dispatchers.IO) {
                try {

                    var weatherModel = iRepo.getWeather(lat, lon, language, units)
                    _allWeather.postValue(weatherModel)
                    //     _allWeather.postValue(weatherModel)
                } catch (e: Exception) {
                    throw e
                }

            }
        } else {

            viewModelScope.launch(Dispatchers.IO) {
        if(!timezone.equals("null")){
         getDataFromDatabase(timezone)
}

            }
        }


    }

    fun getDataFromDatabase(timeZone: String) {
        val response = iRepo.getWeatherByTimeZone(timeZone)
        _allWeather.postValue(response)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo?.isConnected == true
    }
}