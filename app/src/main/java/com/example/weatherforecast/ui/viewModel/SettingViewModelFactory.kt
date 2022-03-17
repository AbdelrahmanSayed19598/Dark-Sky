package com.example.weatherforecast.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.model.RepositoryInterFace

class SettingViewModelFactory(private val iRepo: RepositoryInterFace) :
    ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            SettingViewModel(this.iRepo) as T
        } else {
            throw IllegalArgumentException("View Not Found")
        }
    }
}