package com.example.weatherforecast.homescreen.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Comenecator
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.data.model.Current
import com.example.weatherforecast.homescreen.view.adapter.DailyAdapter
import com.example.weatherforecast.homescreen.view.adapter.HourlyAdapter
import com.example.weatherforecast.homescreen.viewModel.HomeFragmentViewModel
import com.example.weatherforecast.homescreen.viewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.lat
import com.example.weatherforecast.lon
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import java.util.*


class HomeFragment : Fragment() {
    var job: Job? = null;
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: HomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_map2)
             //   bundleOf("userId" to "someUser")

        })

        sharedPreferences = activity?.getSharedPreferences(lat,Context.MODE_PRIVATE)!!
        var latitude  = sharedPreferences.getString("lat","33")
        var longitude = sharedPreferences.getString("lon","-94.04")

        hourly_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        hourly_recycler.hasFixedSize()

        daily_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        daily_recycler.hasFixedSize()

        viewModel =
            ViewModelProvider(this,
                HomeFragmentViewModelFactory(Repository.getRepoInstance(requireActivity().application)))
                .get(HomeFragmentViewModel::class.java)

        viewModel.insertData(latitude, longitude)
        viewModel.allWeather.observe(viewLifecycleOwner) {
            city_name.text = it.timezone.split("/")[1]
            setData(it?.current!!)

            val hourlyAdapter = HourlyAdapter(it?.hourly ?: emptyList())
            hourly_recycler.adapter = hourlyAdapter

            val dailyAdapter = DailyAdapter(it?.daily ?: emptyList())
            daily_recycler.adapter = dailyAdapter
        }



    }


        private fun setData(it: Current) {
        txt_date.text = dateFormat(it.dt.toInt())
        txt_weather.text = it.weather.get(0).main.toString()
        temprature.text = it.temp.toString()
        temprature_unit.text = "°c"
        txt_cloud_value.text = it.clouds.toString() + " %"
        txt_pressure_value.text = it.pressure.toString() + "hpa"
        txt_humiditly_value.text = it.humidity.toString() + " %"
        txt_wind_value.text = it.wind_speed.toString() + " m/s"
        txt_visabilty_value.text = it.visibility.toString() + " m"
        txt_ultra_violate_value.text = it.uvi.toString()
        when (it.weather[0].icon) {

            "01d" -> weather_icon.setImageResource(R.drawable.oned)
            "01n" -> weather_icon.setImageResource(R.drawable.onen)
            "02d" -> weather_icon.setImageResource(R.drawable.twod)
            "02n" -> weather_icon.setImageResource(R.drawable.twon)
            "03d" -> weather_icon.setImageResource(R.drawable.threed)
            "03n" -> weather_icon.setImageResource(R.drawable.threen)
            "04d" -> weather_icon.setImageResource(R.drawable.fourd)
            "04n" -> weather_icon.setImageResource(R.drawable.fourn)
            "09d" -> weather_icon.setImageResource(R.drawable.nined)
            "09n" -> weather_icon.setImageResource(R.drawable.ninen)
            "10d" -> weather_icon.setImageResource(R.drawable.tend)
            "10n" -> weather_icon.setImageResource(R.drawable.tenn)
            "11d" -> weather_icon.setImageResource(R.drawable.eleven_d)
            "11n" -> weather_icon.setImageResource(R.drawable.eleven_n)
            "13d" -> weather_icon.setImageResource(R.drawable.thirteen_d)
            "13n" -> weather_icon.setImageResource(R.drawable.thirteen_n)
            "50d" -> weather_icon.setImageResource(R.drawable.fifty_d)
            "50n" -> weather_icon.setImageResource(R.drawable.fifty_n)
        }

    }

    private fun dateFormat(milliSeconds: Int): String {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
        var month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        var year = calendar.get(Calendar.YEAR).toString()
        return day + " " + month + " " + year

    }
}