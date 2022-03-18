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

class AlertViewModel(private val iRepo: RepositoryInterFace) :
    ViewModel() {
    private var _alertList = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val alertList = _alertList
        fun getAlerts(){
            viewModelScope.launch(Dispatchers.IO) {
                iRepo.getAllAlerts().collect {
                    _alertList.emit(it)
                }
            }
        }
    fun  deleteAlerts(id:Int){
        viewModelScope.launch (Dispatchers.IO){
            iRepo.deleteAlerts(id)
        }
    }

}