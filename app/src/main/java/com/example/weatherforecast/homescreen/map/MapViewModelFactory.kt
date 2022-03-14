package com.example.weatherforecast.homescreen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.homescreen.viewModel.HomeFragmentViewModel

class MapViewModelFactory(private val iRepo: RepositoryInterFace) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(this.iRepo) as T
        } else {
            throw IllegalArgumentException("View Not Found")
        }
    }

}