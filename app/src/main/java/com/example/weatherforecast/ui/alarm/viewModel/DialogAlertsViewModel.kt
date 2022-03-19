package com.example.weatherforecast.ui.alarm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.data.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DialogAlertsViewModel(private val iRepo: RepositoryInterFace) :
    ViewModel() {
    private var _id : MutableLiveData<Long> = MutableLiveData()
    val id =_id

    fun  insertAlerts(weatherAlert: WeatherAlert){
        viewModelScope.launch (Dispatchers.IO){
           id.postValue(iRepo.insertAlert(weatherAlert))
        }
    }

}