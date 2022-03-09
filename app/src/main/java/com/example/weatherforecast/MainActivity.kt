package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weatherforecast.alerts.AlertFragment
import com.example.weatherforecast.favorite.FavoriteFragment
import com.example.weatherforecast.homescreen.view.HomeFragment
import com.example.weatherforecast.settings.SettingFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var togel : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
     var id : Int = 0
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