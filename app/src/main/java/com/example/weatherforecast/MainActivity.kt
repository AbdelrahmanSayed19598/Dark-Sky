package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weatherforecast.homescreen.view.fragment.AlertFragment
import com.example.weatherforecast.homescreen.view.fragment.FavoriteFragment
import com.example.weatherforecast.homescreen.view.fragment.HomeFragment
import com.example.weatherforecast.homescreen.view.fragment.SettingFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView


const val lat = "lat"
const val lon = "lon"

class MainActivity : AppCompatActivity() {
    lateinit var togel : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor


     var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermision()

        sharedPreferences = getSharedPreferences(lat,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()



       drawerLayout  = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.navView)

        togel = ActionBarDrawerToggle(this,drawerLayout,R.string.open  ,R.string.close)

        drawerLayout.addDrawerListener(togel)
        togel.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    id = R.id.nav_home
                    replaceFragment(HomeFragment(),it.title.toString())
                drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.nav_favorite -> {
                    id = R.id.nav_favorite
                    replaceFragment(FavoriteFragment(),it.title.toString())
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_alert -> {
                    id = R.id.nav_alert
                    replaceFragment(AlertFragment(),it.title.toString())
                    drawerLayout.closeDrawer(GravityCompat.START)

                }
                R.id.nav_setting ->{
                    id = R.id.nav_setting
                    replaceFragment(SettingFragment(), it.title.toString())
            drawerLayout.closeDrawer(GravityCompat.START)
                 }
            }
            true
        }

    }

    private fun checkLocationPermision() {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                Log.i("TAG", "checkLocationPermision: ")
                Toast.makeText(this,"${it.latitude}+ ${it.longitude}",Toast.LENGTH_SHORT).show()
                editor.putString(lat,it.latitude.toString())
                editor.putString(lon,it.longitude.toString())
                editor.apply()
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (togel.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun replaceFragment(fragment : Fragment, title : String){
        val fragmentManager = supportFragmentManager
        val frahmentTransaction =fragmentManager.beginTransaction()
        frahmentTransaction.replace(R.id.openning_fragment_home,fragment)
        frahmentTransaction.commit()
        setTitle(title)
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
       else if (id != R.id.nav_home){
            replaceFragment(HomeFragment(),"Home")
            id = R.id.nav_home
        }
        else {
            super.onBackPressed()
        }
    }
}