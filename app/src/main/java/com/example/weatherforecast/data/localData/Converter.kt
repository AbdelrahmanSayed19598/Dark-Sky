package com.example.weatherforecast.data.localData

import androidx.room.TypeConverter
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Daily
import com.example.weatherforecast.data.model.Hourly
import com.example.weatherforecast.data.model.Weather
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun listHourlyToJson(value: List<Hourly>?) = Gson().toJson(value)

    @TypeConverter
    fun listDailyToJson(value: List<Daily>) = Gson().toJson(value)

    @TypeConverter
    fun listAlertToJson(value: List<Alert>?) = Gson().toJson(value)

    @TypeConverter
    fun listWeatherToJson(value: List<Weather>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToWeatherList(value: String) =
        Gson().fromJson(value, Array<Weather>::class.java).toList()

    @TypeConverter
    fun jsonToHourlyList(value: String) = Gson().fromJson(value, Array<Hourly>::class.java)?.toList()

    @TypeConverter
    fun jsonToDailyList(value: String) = Gson().fromJson(value, Array<Daily>::class.java).toList()

    @TypeConverter
    fun jsonToAlertList(value: String?): List<Alert>? {
        value?.let {
            return Gson().fromJson(value, Array<Alert>::class.java)?.toList()
        }
        return emptyList()
    }
}