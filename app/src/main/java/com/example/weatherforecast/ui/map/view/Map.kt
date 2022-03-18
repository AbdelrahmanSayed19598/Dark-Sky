package com.example.weatherforecast.ui.map.view

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.activity.lon
import com.example.weatherforecast.ui.map.viewModel.MapViewModel
import com.example.weatherforecast.ui.map.viewModel.MapViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class Map : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var lang: String
    lateinit var unit: String
    lateinit var navControler: NavController

    var flag: Boolean = false

    private lateinit var viewModel: MapViewModel


    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            latitude = it.latitude
            longitude = it.longitude

        }
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        latitude = sharedPreferences.getString("lat", "0")?.toDouble()!!
        longitude = sharedPreferences.getString("lon", "0")?.toDouble()!!
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        flag = arguments?.getBoolean("mapArg")!!
        navControler = Navigation.findNavController(view)
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.tool_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnSave -> {
                lang = sharedPreferences.getString("lang", "en").toString()
                unit = sharedPreferences.getString("unit", "metric").toString()
                viewModel =
                    ViewModelProvider(
                        this,
                        MapViewModelFactory(Repository.getRepoInstance(requireActivity().application))
                    ).get(MapViewModel::class.java)
                if (isNetworkAvailable(requireContext())) {
                    if (flag == false) {
                        sharedPreferences =
                            requireActivity().getSharedPreferences(lat, Context.MODE_PRIVATE)

                        viewModel.insertData(latitude.toString(), longitude.toString(), lang, unit)

                        editor = sharedPreferences.edit()

                        editor.putString(lat, latitude.toString())
                        editor.putString(lon, longitude.toString())
                        editor.putString("map", "0")
                        editor.apply()
                        navControler.navigate(R.id.homeFragment)
                        Toast.makeText(
                            requireContext(),
                            "chosen country gotten successfully ",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.insertFavoritePlace(
                            latitude.toString(),
                            longitude.toString(),
                            lang,
                            unit
                        )

                        navControler.navigate(R.id.favoriteFragment)
                        Toast.makeText(
                            requireContext(),
                            "this country saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "no internet please check your network",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
        return super.onOptionsItemSelected(item)

    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo?.isConnected == true
    }
}