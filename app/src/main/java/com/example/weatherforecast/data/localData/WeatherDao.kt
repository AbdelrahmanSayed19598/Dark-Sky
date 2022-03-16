package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM Weather WHERE isFav=1")
    fun getAll(): Flow<List<WeatherModel>>

    @Query("SELECT * FROM Weather WHERE timezone = :timezone ")
    fun getWeatherByTimeZone(timezone: String): WeatherModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherModel)

    @Query("DELETE FROM Weather WHERE timezone=:timezone")
    fun deleteByTimeZone(timezone: String?)

    @Query("SELECT * FROM Weather WHERE timezone = :timezone AND isFav=0")
    fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel

    @Query("DELETE FROM Weather WHERE isFav=0")
    fun deleteNotFav()



}