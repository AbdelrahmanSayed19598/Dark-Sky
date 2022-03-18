package com.example.weatherforecast.ui.setting.viewModel

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.data.model.RepositoryInterFace

class SettingViewModel (private val iRepo: RepositoryInterFace) : ViewModel() {
    fun refreshData(){
        iRepo.refrechData()
    }
}