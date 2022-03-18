package com.example.weatherforecast.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int? =null,
    var startTime: Long,
    var endTime: Long,
    var startDate: Long,
    var endDate: Long
)