package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.model.WeatherModel

@Dao
interface WeatherDao {
    @Query("SELECT * FROM Weather")
    fun getAll(): LiveData<List<WeatherModel>>

    @Query("SELECT * FROM Weather WHERE lat=:lat AND lon=:lng ")
    fun getWeatherResponse(lat:String,lng:String): LiveData<WeatherModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherModel)

    @Query("DELETE FROM Weather")
    suspend fun deleteAll()
}