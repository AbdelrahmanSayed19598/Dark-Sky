package com.example.weatherforecast.ui.alarm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.model.RepositoryInterFace

class AlertViewModelFactory(private val iRepo:RepositoryInterFace): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertViewModel(iRepo) as T
    }
}