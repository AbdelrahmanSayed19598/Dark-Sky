package com.example.weatherforecast.ui.setting.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weatherforecast.ui.activity.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.activity.lon
import com.example.weatherforecast.ui.setting.viewModel.SettingViewModel
import com.example.weatherforecast.ui.setting.viewModel.SettingViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*

class SettingFragment : Fragment() {
    lateinit var viewModel: SettingViewModel
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var navControler: NavController
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(
                this,
                SettingViewModelFactory(Repository.getRepoInstance(requireActivity().application))
            )
                .get(SettingViewModel::class.java)


        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navControler = Navigation.findNavController(view)

        var lang = sharedPreferences.getString("lang", "en")
        var unit = sharedPreferences.getString("unit", "metric")
        when (lang) {
            "en" -> englishRadio.isChecked = true
            "ar" -> arabicRadio.isChecked = true
        }

        when (unit) {
            "metric" -> celsiusRadio.isChecked = true
            "imperial" -> fahrenhietRadio.isChecked = true
            "standard" -> kelvinRadio.isChecked = true
        }
        gpsRadio.isChecked = true

        arabicRadio.setOnClickListener(View.OnClickListener {
            changeLang("ar")
        })

        englishRadio.setOnClickListener(View.OnClickListener {
            changeLang("en")
        })

        celsiusRadio.setOnClickListener(View.OnClickListener {
            changeUnite("metric")
        })
        kelvinRadio.setOnClickListener(View.OnClickListener {
            changeUnite("standard")
        })
        fahrenhietRadio.setOnClickListener(View.OnClickListener {
            changeUnite("imperial")
        })

        gpsRadio.setOnClickListener(View.OnClickListener {
            getcurrentLocation()
        })
        mapRadio.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_map)
        })


    }


    private fun changeUnite(units: String) {
        editor.putString("unit", units)
        editor.apply()
        navControler.navigate(R.id.homeFragment)

    }


    private fun changeLang(lang: String) {
        editor.putString("lang", lang)
        editor.apply()
        viewModel.refreshData()
        setLocale(lang)
        //navControler.navigate(R.id.homeFragment)
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        Locale.setDefault(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val refresh = Intent(requireContext(), MainActivity::class.java)
        activity?.finish()

        startActivity(refresh)

    }


    private fun getcurrentLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermision()

        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()
        Toast.makeText(
            requireContext(),
            getString(R.string.map_selection),
            Toast.LENGTH_SHORT
        ).show()

        val refresh = Intent(requireContext(), MainActivity::class.java)
        activity?.finish()

        startActivity(refresh)
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
}