package com.example.weatherforecast.ui.favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.model.RepositoryInterFace

class FavoriteViewModelFactory (private val iRepo: RepositoryInterFace) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(this.iRepo) as T
        } else {
            throw IllegalArgumentException("View Not Found")
        }    }
}