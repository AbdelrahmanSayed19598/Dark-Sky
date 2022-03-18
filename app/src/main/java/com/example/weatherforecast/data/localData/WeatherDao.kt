package com.example.weatherforecast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecast.data.model.WeatherAlert
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

    @Query("DELETE FROM Weather WHERE timezone= :timezone")
    fun deleteByTimeZone(timezone: String?)

    @Query("SELECT * FROM Weather WHERE timezone = :timezone AND isFav=0")
    fun getWeatherByTimeZoneAndNotFav(timezone: String): WeatherModel

    @Query("DELETE FROM Weather WHERE isFav=0")
    fun deleteNotFav()

    @Query("SELECT * FROM Weather WHERE isFav=1")
    fun getAllFavoriteData(): List<WeatherModel>

    @Query("SELECT * FROM Weather WHERE lat=:lat AND lon=:lng ")
    fun getWeatherByLatLong(lat: String, lng: String): WeatherModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertAlert(weatherAlert: WeatherAlert)

    @Query("SELECT * FROM alert")
    fun getAllAlerts():Flow<List<WeatherAlert>>

    @Query("DELETE FROM alert WHERE id =:id")
    suspend fun deleteAlerts(id:Int)



}