package com.example.weatherforecast.ui.alarm.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.data.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DialogAlertsViewModel(private val iRepo: RepositoryInterFace) :
    ViewModel() {

    fun  insertAlerts(weatherAlert: WeatherAlert){
        viewModelScope.launch (Dispatchers.IO){
            iRepo.insertAlert(weatherAlert)
        }
    }

}