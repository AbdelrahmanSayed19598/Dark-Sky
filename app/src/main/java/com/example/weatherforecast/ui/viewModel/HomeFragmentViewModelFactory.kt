package com.example.weatherforecast.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.model.RepositoryInterFace

class HomeFragmentViewModelFactory(private val iRepo: RepositoryInterFace) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            HomeFragmentViewModel(this.iRepo) as T
        } else {
            throw IllegalArgumentException("View Not Found")
        }    }
}