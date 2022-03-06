package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weatherforecast.alerts.AlertFragment
import com.example.weatherforecast.favorite.FavoriteFragment
import com.example.weatherforecast.homescreen.HomeFragment
import com.example.weatherforecast.settings.SettingFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var togel : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       drawerLayout  = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.navView)

        togel = ActionBarDrawerToggle(this,drawerLayout,R.string.open  ,R.string.close)

        drawerLayout.addDrawerListener(togel)
        togel.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> replaceFragment(HomeFragment(),it.title.toString())
                R.id.nav_favorite -> replaceFragment(FavoriteFragment(),it.title.toString())
                R.id.nav_alert -> replaceFragment(AlertFragment(),it.title.toString())
                R.id.nav_setting -> replaceFragment(SettingFragment(),it.title.toString())
            }
            true
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
        frahmentTransaction.replace(R.id.frameLayout,fragment)
        frahmentTransaction.commit()
        setTitle(title)
    }
}