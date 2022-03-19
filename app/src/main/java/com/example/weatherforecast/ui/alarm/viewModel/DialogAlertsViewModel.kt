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
    var result : Long? = null
    private var _id : MutableLiveData<Long> = MutableLiveData()
    val id =_id

    fun  insertAlerts(weatherAlert: WeatherAlert){
        viewModelScope.launch (Dispatchers.IO){
            val job = viewModelScope.launch(Dispatchers.IO) {
                result= iRepo.insertAlert(weatherAlert)

            }
            job.join()
            if (result!=null){
                _id.postValue(result)
            }
        }
    }

}