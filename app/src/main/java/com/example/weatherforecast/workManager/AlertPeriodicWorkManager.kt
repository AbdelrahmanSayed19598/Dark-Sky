package com.example.weatherforecast.workManager

import android.content.Context
import androidx.work.*
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.data.model.WeatherAlert
import com.example.weatherforecast.ui.activity.timeZoneShared
import java.util.*
import java.util.concurrent.TimeUnit

class AlertPeriodicWorkManager(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {
    val weatherRepository = Repository.getRepoInstance(context)
    override suspend fun doWork(): Result {
        val inputData = inputData
        val id = inputData.getString("ID").toString()
        val timeZone = inputData.getString(timeZoneShared).toString()
        getCurrentData(id.toInt(), timeZone)
        return Result.success()
    }

    private fun getCurrentData(id: Int, timeZone: String) {
        val currentWeather = weatherRepository.getWeatherByTimeZone(timeZone)
        val alert = weatherRepository.getAlert(id)
        var delay = getPeriod(alert)
      //  val current = Calendar.getInstance().timeInMillis
        if(checkTime(alert)){
            if(currentWeather.alerts.isNullOrEmpty()){

                setOneTimeWorkManager(delay,id.toInt(),currentWeather.current.weather[0].description,currentWeather.current.weather[0].icon)
            }else{
                setOneTimeWorkManager(delay,id.toInt(),currentWeather.alerts[0].event,currentWeather.current.weather[0].icon)
            }

        }

    }

    private fun checkTime(alert: WeatherAlert):Boolean {
    val current = Calendar.getInstance().timeInMillis
        return current > alert.startDate && current < alert.endDate
    }



    private fun getPeriod(alert: WeatherAlert): Long {
        val hour = TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.startTime - (hour + minute)
    }

    private fun setOneTimeWorkManager( delay:Long,id:Int,description:String,icon:String){

        val data = Data.Builder()
            .putString("description",description)
            .putString("icon",icon)
            .build()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            OnTimeWorkManger::class.java
        )
            .setInputData(data)
            .setConstraints(constraints)
            .addTag(id.toString())
            .setInitialDelay(delay,TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            id.toString(), ExistingWorkPolicy.REPLACE
            ,oneTimeWorkRequest
        )

    }
}
