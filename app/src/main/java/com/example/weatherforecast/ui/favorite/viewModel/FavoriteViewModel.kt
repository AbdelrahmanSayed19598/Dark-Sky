package com.example.weatherforecast.ui.favorite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteViewModel (private val iRepo: RepositoryInterFace) : ViewModel() {

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            iRepo.getAllWather().collect {
                _favoriteList.postValue(it)
            }
        }

    }
    private var _favoriteList = MutableLiveData<List<WeatherModel>>()
    val favoriteList = _favoriteList


}