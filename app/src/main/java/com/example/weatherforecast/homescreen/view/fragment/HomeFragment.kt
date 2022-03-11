package com.example.weatherforecast.homescreen.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.ApiWeatherService
import com.example.weatherforecast.data.RetrofitHelper
import com.example.weatherforecast.data.response.Current
import com.example.weatherforecast.homescreen.view.adapter.DailyAdapter
import com.example.weatherforecast.homescreen.view.adapter.HourlyAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.hourly_weather_row.*
import kotlinx.coroutines.*
import java.util.*


class HomeFragment : Fragment() {
    var job: Job? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourly_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        hourly_recycler.hasFixedSize()

        daily_recycler.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        daily_recycler.hasFixedSize()



        lifecycle.coroutineScope.launch{
            job = CoroutineScope(Dispatchers.IO).launch {
                val weatherData =RetrofitHelper.getInstance().create(ApiWeatherService::class.java)
                val response = weatherData.getWeather(33.44,-94.04)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){

                        city_name.text = response.body()?.timezone.toString().split("/")[1]
                        setData(response.body()?.current!!)

                        val hourlyAdapter = HourlyAdapter(response.body()?.hourly?: emptyList())
                        hourly_recycler.adapter = hourlyAdapter

                        val dailyAdapter = DailyAdapter(response.body()?.daily?: emptyList())
                        daily_recycler.adapter = dailyAdapter
                    }
                }
            }
        }




    }

private fun setData(it:Current){
    txt_date.text = dateFormat(it.dt.toInt())
    txt_weather.text = it.weather.get(0).main.toString()
    temprature.text = it.temp.toString()
    temprature_unit.text ="°c"
    txt_cloud_value.text = it.clouds.toString()+ " %"
    txt_pressure_value.text = it.pressure.toString()+ "hpa"
    txt_humiditly_value.text = it.humidity.toString()+ " %"
    txt_wind_value.text = it.windSpeed.toString()+ " m/s"
    txt_visabilty_value.text =it.visibility.toString()+ " m"
    when(it.weather.get(0).icon){

        "01d"-> weather_icon.setImageResource(R.drawable.oned)
        "01n"-> weather_icon.setImageResource(R.drawable.onen)
    }

}
    private fun dateFormat(milliSeconds: Int):String{
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
        var month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        var day=calendar.get(Calendar.DAY_OF_MONTH).toString()
        var year=calendar.get(Calendar.YEAR).toString()
        return day+" "+month +" "+year

    }
}