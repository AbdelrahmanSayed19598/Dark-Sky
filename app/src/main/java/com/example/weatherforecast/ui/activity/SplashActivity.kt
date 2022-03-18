package com.example.weatherforecast.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var lang: String
    lateinit var unit: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences = getSharedPreferences(lat, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        lang = sharedPreferences.getString("lang", "en").toString()
        unit = sharedPreferences.getString("unit", "metric").toString()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermision()
        logo_splash.setAnimation(
            AnimationUtils.loadAnimation(this, R.anim.up_to_down_anim)
        )
        logo_name_1.setAnimation(
            AnimationUtils.loadAnimation(this, R.anim.left_to_right_anim)
        )
        logo_name_2.setAnimation(
            AnimationUtils.loadAnimation(this, R.anim.right_to_left_anim)
        )

        lifecycleScope.launch(Dispatchers.IO) {
            delay(4000)
            val refresh = Intent(this@SplashActivity, MainActivity::class.java)
            finish()

            startActivity(refresh)

        }


    }

    private fun checkLocationPermision() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                editor.putString(lat, it.latitude.toString())
                editor.putString(lon, it.longitude.toString())
                editor.putString("lang", lang)
                editor.putString("unit", unit)

                editor.putString("map", "0")

                editor.apply()
            }
        }


    }
}