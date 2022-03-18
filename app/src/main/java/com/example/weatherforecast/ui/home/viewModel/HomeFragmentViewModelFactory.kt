package com.example.weatherforecast.ui.home.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.model.RepositoryInterFace

class HomeFragmentViewModelFactory(private val iRepo: RepositoryInterFace,private val  context: Context) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            HomeFragmentViewModel(this.iRepo,context ) as T
        } else {
            throw IllegalArgumentException("View Not Found")
        }    }
}