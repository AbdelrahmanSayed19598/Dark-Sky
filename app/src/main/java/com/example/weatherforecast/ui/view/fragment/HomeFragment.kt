package com.example.weatherforecast.ui.view.fragment

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
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
import com.example.weatherforecast.ui.view.adapter.DailyAdapter
import com.example.weatherforecast.ui.view.adapter.HourlyAdapter
import com.example.weatherforecast.ui.viewModel.HomeFragmentViewModel
import com.example.weatherforecast.ui.viewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.lat
import com.example.weatherforecast.lon
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
        navControler = Navigation.findNavController(view)


        map_btn_from_home.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_map2)
        })

        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        latitude = sharedPreferences.getString("lat", "0").toString()
        longitude = sharedPreferences.getString("lon", "0").toString()
        lang = sharedPreferences.getString("lang", "en").toString()
        unit = sharedPreferences.getString("unit", "metric").toString()

        hourly_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        hourly_recycler.hasFixedSize()

        daily_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        daily_recycler.hasFixedSize()

        viewModel =
            ViewModelProvider(
                this,
                HomeFragmentViewModelFactory(Repository.getRepoInstance(requireActivity().application))
            )
                .get(HomeFragmentViewModel::class.java)
        getDataForDisplay()


    }


    private fun setData(it: Current) {
        txt_date.text = dateFormat(it.dt.toInt())
        txt_weather.text = it.weather[0].description.toString()
        temprature.text = it.temp.toInt().toString()
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
    private fun getCityName(lat: Double, lon: Double): String {
        var city = ""
        val geocoder = Geocoder(context, Locale(lang))
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea // damietta
            val country = addresses[0].countryName
            city = "$state, $country"
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

                sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
                editor = sharedPreferences.edit()
                Toast.makeText(
                    requireContext(),
                    "current location gotten Succesfully",
                    Toast.LENGTH_SHORT
                ).show()

                navControler.navigate(R.id.homeFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkLocationPermision() {
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


    }

    fun getDataForDisplay() {
        try {
            viewModel.getData(latitude, longitude,lang,unit)
            viewModel.allWeather.observe(viewLifecycleOwner) {
                city_name.text = getCityName(it.lat,it.lon)
                setData(it?.current!!)

                val hourlyAdapter = HourlyAdapter(it?.hourly ?: emptyList(),requireContext())
                hourly_recycler.adapter = hourlyAdapter

                val dailyAdapter = DailyAdapter(it?.daily ?: emptyList(),requireContext())
                daily_recycler.adapter = dailyAdapter
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}