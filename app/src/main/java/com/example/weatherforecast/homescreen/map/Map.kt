package com.example.weatherforecast.homescreen.map

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.homescreen.viewModel.HomeFragmentViewModel
import com.example.weatherforecast.homescreen.viewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.lat
import com.example.weatherforecast.lon

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Map : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var navControler: NavController

    private lateinit var viewModel: MapViewModel


    private val callback = OnMapReadyCallback { googleMap ->

        // val location = LatLng(latitude, longitude)
//        googleMap.addMarker(MarkerOptions().position(location))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
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
        latitude = sharedPreferences.getString("lat", "33")?.toDouble()!!
        longitude = sharedPreferences.getString("lon", "-94.04")?.toDouble()!!
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
                viewModel =
                    ViewModelProvider(
                        this,
                        MapViewModelFactory(Repository.getRepoInstance(requireActivity().application))
                    ).get(MapViewModel::class.java)
                viewModel.insertData(latitude.toString(), longitude.toString())

                sharedPreferences =
                    requireActivity().getSharedPreferences(lat, Context.MODE_PRIVATE)
                editor = sharedPreferences.edit()

                editor.putString(lat, latitude.toString())
                editor.putString(lon, longitude.toString())
                editor.apply()
                Toast.makeText(
                    requireContext(),
                    "this country saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
                navControler.navigate(R.id.homeFragment)
            }
        }
        return super.onOptionsItemSelected(item)

    }
}