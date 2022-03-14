package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView


const val lat = "lat"
const val lon = "lon"

class MainActivity : AppCompatActivity() {
//    lateinit var togel : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    private lateinit var appBarConfiguration: AppBarConfiguration


     var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout  = findViewById(R.id.drawerLayout)

        val navView : NavigationView = findViewById(R.id.navView)
        val navController = findNavController(R.id.openning_fragment_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,R.id.favoriteFragment,R.id.alertFragment,R.id.settingFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)




//        val fragmentManager = supportFragmentManager
//        val frahmentTransaction =fragmentManager.beginTransaction()
//        frahmentTransaction.add(R.id.openning_fragment_home,HomeFragment(),"Home")
//        frahmentTransaction.commit()
//        setTitle("Home")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermision()

        sharedPreferences = getSharedPreferences(lat,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()





//        togel = ActionBarDrawerToggle(this,drawerLayout,R.string.open  ,R.string.close)
//
//        drawerLayout.addDrawerListener(togel)
//        togel.syncState()
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        navView.setNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.homeFragment -> {
//                    id = R.id.homeFragment
//                    replaceFragment(HomeFragment(this),it.title.toString())
//                drawerLayout.closeDrawer(GravityCompat.START)
//                }
//
//                R.id.favoriteFragment -> {
//                    id = R.id.favoriteFragment
//                    replaceFragment(FavoriteFragment(),it.title.toString())
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                }
//                R.id.nav_alert -> {
//                    id = R.id.nav_alert
//                    replaceFragment(AlertFragment(),it.title.toString())
//                    drawerLayout.closeDrawer(GravityCompat.START)
//
//                }
//                R.id.nav_setting ->{
//                    id = R.id.nav_setting
//                    replaceFragment(SettingFragment(), it.title.toString())
//            drawerLayout.closeDrawer(GravityCompat.START)
//                 }
//            }
//            true
//        }

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
              editor.putString(lat,it.latitude.toString())
                editor.putString(lon,it.longitude.toString())
                editor.apply()
            }
        }

    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (togel.onOptionsItemSelected(item)){
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }


//    private fun replaceFragment(fragment : Fragment, title : String){
//        val fragmentManager = supportFragmentManager
//        val frahmentTransaction =fragmentManager.beginTransaction()
//        frahmentTransaction.replace(R.id.openning_fragment_home,fragment)
//        frahmentTransaction.commit()
//        setTitle(title)
//    }

//    override fun onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START)
//        }
//       else if (id != R.id.nav_home){
//            replaceFragment(HomeFragment(this),"Home")
//            id = R.id.nav_home
//        }
//        else {
//            super.onBackPressed()
//        }
//    }

//    override fun onClick() {
////        replaceFragment(Map(),"Map")
//    }
}



//package com.example.weatherapp
//
//import android.os.Bundle
//import android.view.Menu
//import com.google.android.material.snackbar.Snackbar
//import com.google.android.material.navigation.NavigationView
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.appcompat.app.AppCompatActivity
//import com.example.weatherapp.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.appBarMain.toolbar)
//
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//
//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//
//            navController.navigate(R.id.action_nav_home_to_selectLocationFragment)
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
//}