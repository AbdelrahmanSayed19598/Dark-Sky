package com.example.weatherforecast.ui.home.view.fragment

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.data.model.Current
import com.example.weatherforecast.ui.home.view.adapter.DailyAdapter
import com.example.weatherforecast.ui.home.view.adapter.HourlyAdapter
import com.example.weatherforecast.ui.home.viewModel.HomeFragmentViewModel
import com.example.weatherforecast.ui.home.viewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.activity.lon
import com.example.weatherforecast.ui.activity.timeZoneShared
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {
    lateinit var navControler: NavController
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var longitude: String
    lateinit var latitude: String
    lateinit var lang: String
    lateinit var unit: String
    lateinit var timeZoneSh: String
    private lateinit var viewModel: HomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swiperefresh.visibility = View.VISIBLE
        home_fragment_layout.visibility = View.INVISIBLE
        navControler = Navigation.findNavController(view)


        map_btn_from_home.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_map2)
        })

        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()
        latitude = sharedPreferences.getString("lat", "0").toString()
        longitude = sharedPreferences.getString("lon", "0").toString()
        lang = sharedPreferences.getString("lang", "en").toString()
        unit = sharedPreferences.getString("unit", "metric").toString()
        timeZoneSh = sharedPreferences.getString(timeZoneShared, "null").toString()

        hourly_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        hourly_recycler.hasFixedSize()

        daily_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        daily_recycler.hasFixedSize()

        viewModel =
            ViewModelProvider(
                this,
                HomeFragmentViewModelFactory(
                    Repository.getRepoInstance(requireActivity().application),
                    requireContext()
                )
            )[HomeFragmentViewModel::class.java]
        getDataForDisplay()


    }


    private fun setData(it: Current) {
        txt_date.text = dateFormat(it.dt)
        txt_weather.text = it.weather[0].description
        temprature.text = arabicToEnglish(it.temp.toInt().toString(), lang)
        when (unit) {
            "metric" -> temprature_unit.text = "°c"
            "imperial" -> temprature_unit.text = "°f"
            "standard" -> temprature_unit.text = "°k"
        }

        txt_cloud_value.text = arabicToEnglish(it.clouds.toString(), lang) + " %"
        txt_pressure_value.text = arabicToEnglish(it.pressure.toString(), lang) + "hpa"
        txt_humiditly_value.text = arabicToEnglish(it.humidity.toString(), lang) + " %"
        when (unit) {
            "imperial" -> txt_wind_value.text =
                arabicToEnglish(it.wind_speed.toString(), lang) + " m/h"
            "metric" -> txt_wind_value.text =
                arabicToEnglish(it.wind_speed.toString(), lang) + " m/s"
            "standard" -> txt_wind_value.text =
                arabicToEnglish(it.wind_speed.toString(), lang) + " m/s"
        }

        txt_visabilty_value.text = arabicToEnglish(it.visibility.toString(), lang) + " m"
        txt_ultra_violate_value.text = arabicToEnglish(it.uvi.toString(), lang)
        when (it.weather[0].icon) {

            "01d" -> weather_icon.setImageResource(R.drawable.ic_sun_svgrepo_com)
            "01n" -> weather_icon.setImageResource(R.drawable.ic_moon_svgrepo_com)
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
            "13d" -> weather_icon.setImageResource(R.drawable.ic_snow_svgrepo_com)
            "13n" -> weather_icon.setImageResource(R.drawable.ic_snow_svgrepo_com)
            "50d" -> weather_icon.setImageResource(R.drawable.fifty_d)
            "50n" -> weather_icon.setImageResource(R.drawable.fifty_n)
        }
    }

    private fun getCityName(lat: Double, lon: Double): String {
        var city = ""
        val geocoder = Geocoder(context, Locale(lang))
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea // damietta
            val country = addresses[0].countryName
            city = "$state"
        }
        return city
    }

    private fun dateFormat(milliSeconds: Int): String {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
        var month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale(lang))
        var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        var year = calendar.get(Calendar.YEAR).toString()
        return day + " " + month + " " + year

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.location_current, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnCurrent -> {

                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                checkLocationPermision()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.current_toast),
                    Toast.LENGTH_SHORT
                ).show()

                navControler.navigate(R.id.homeFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkLocationPermision() {
        if (isNetworkAvailable(requireContext())) {
            val task = fusedLocationProviderClient.lastLocation

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
                return
            }
            task.addOnSuccessListener {
                if (it != null) {
                    editor.putString(lat, it.latitude.toString())
                    editor.putString(lon, it.longitude.toString())
                    editor.putString("map", "0")
                    editor.apply()
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.check_network),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun getDataForDisplay() {
        try {
            if (!isNetworkAvailable(requireContext())) {
                when (timeZoneSh) {
                    "null" -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.noCountry),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else ->
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.check_network),
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
            viewModel.getData(latitude, longitude, lang, unit)
            viewModel.allWeather.observe(viewLifecycleOwner) {
                swiperefresh.visibility = View.INVISIBLE
                home_fragment_layout.visibility = View.VISIBLE
                city_name.text = getCityName(it.lat, it.lon)
                setData(it?.current!!)

                val hourlyAdapter = HourlyAdapter(it?.hourly ?: emptyList(), requireContext())
                hourly_recycler.adapter = hourlyAdapter

                val dailyAdapter = DailyAdapter(it?.daily ?: emptyList(), requireContext())
                daily_recycler.adapter = dailyAdapter
            }
        } catch (e: Exception) {
            swiperefresh.visibility = View.INVISIBLE
            Toast.makeText(
                requireContext(),
                "${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun arabicToEnglish(str: String, lang: String): String {

        if (lang.equals("ar")) {
            var result = ""
            var en = '0'
            for (ch in str) {
                en = ch
                when (ch) {
                    '0' -> en = '۰'
                    '1' -> en = '۱'
                    '2' -> en = '۲'
                    '3' -> en = '۳'
                    '4' -> en = '٤'
                    '5' -> en = '۵'
                    '6' -> en = '٦'
                    '7' -> en = '۷'
                    '8' -> en = '۸'
                    '9' -> en = '۹'
                }
                result = "${result}$en"
            }
            return result
        } else {
            return str
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo?.isConnected == true
    }
}