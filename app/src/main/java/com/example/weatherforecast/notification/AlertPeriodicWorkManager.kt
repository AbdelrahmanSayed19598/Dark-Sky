package com.example.weatherforecast.notification

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.*
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.data.model.WeatherAlert
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.activity.timeZoneShared
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertPeriodicWorkManager(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var language: String
    val weatherRepository = Repository.getRepoInstance(context)
    override suspend fun doWork(): Result {
        Log.i("as", "getCurrentData: doWork")
        val inputData = inputData
        val id = inputData.getInt("ID",0).toString()
        val timeZone = inputData.getString(timeZoneShared).toString()
        getCurrentData(id.toInt(), timeZone)
        return Result.success()
    }

    private suspend fun getCurrentData(id: Int, timeZone: String) {
        val currentWeather = weatherRepository.getWeatherByTimeZone(timeZone)
        val alert = weatherRepository.getAlert(id)
        var delay = getPeriod(alert)
        //  val current = Calendar.getInstance().timeInMillis
        if (checkTime(alert)) {
            Log.i("as", "getCurrentData: true")
            if (currentWeather.alerts.isNullOrEmpty()) {

                setOneTimeWorkManager(
                    delay,
                    id,
                    currentWeather.current.weather[0].description,
                    currentWeather.current.weather[0].icon
                )
            } else {
                setOneTimeWorkManager(
                    delay,
                    id,
                    currentWeather.alerts[0].event,
                    currentWeather.current.weather[0].icon
                )
            }

        } else {
           weatherRepository.deleteAlerts(id)
        }

    }

  private fun checkTime(alert: WeatherAlert): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        return (dayNow in alert.startDate..alert.endDate)
    }


    private fun getPeriod(alert: WeatherAlert): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.startTime - ((hour + minute) - (2 * 3600L))
    }



    private fun setOneTimeWorkManager(delay: Long, id: Int, description: String, icon: String) {

        val data = Data.Builder()
            .putString("description", description)
            .putString("icon", icon)
            .build()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            OnTimeWorkManger::class.java
        )
            .setInputData(data)
            .setConstraints(constraints)
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            id.toString(), ExistingWorkPolicy.REPLACE, oneTimeWorkRequest
        )

    }
    private fun getDateMillis(date: String): Long {
        sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        language = sharedPreferences.getString("lang", "en").toString()
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(language))
        val d: Date = f.parse(date)
        return d.time
    }
}
