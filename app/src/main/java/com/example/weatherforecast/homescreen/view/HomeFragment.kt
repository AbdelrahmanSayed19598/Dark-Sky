package com.example.weatherforecast.homescreen.view

import android.os.Bundle
import android.util.Log
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
import com.example.weatherforecast.model.HoursPojo
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import retrofit2.create


class HomeFragment : Fragment() {
    val hoursPojo = ArrayList<HoursPojo>()
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



        lifecycle.coroutineScope.launch{
            job = CoroutineScope(Dispatchers.IO).launch {
                val weatherData =RetrofitHelper.getInstance().create(ApiWeatherService::class.java)
                val response = weatherData.getWeather(33.44,-94.04)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        setData(response.body()?.current!!)
                        val myadaptet = HourlyAdapter(response.body()?.hourly?: emptyList())

                        hourly_recycler.adapter = myadaptet
                    }
                }
            }
        }




    }

private fun setData(it:Current){



    txt_weather.text = it.weather.get(0).main.toString()
    temprature.text = it.temp.toString()
    temprature_unit.text ="°c"
    txt_cloud_value.text = it.clouds.toString()+ " %"
    txt_pressure_value.text = it.pressure.toString()+ "hpa"
    txt_humiditly_value.text = it.humidity.toString()+ " %"
    txt_wind_value.text = it.windSpeed.toString()+ " m/s"
    txt_visabilty_value.text =it.visibility.toString()+ " m"

}
}