package com.example.weatherforecast.ui.favorite.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.data.model.WeatherModel
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.activity.lon
import com.example.weatherforecast.ui.activity.timeZoneShared
import com.example.weatherforecast.ui.favorite.viewModel.FavoriteViewModel
import kotlinx.android.synthetic.main.fav_row.view.*
import java.util.*

class FavoriteAdapter(val weatherModels: List<WeatherModel>,val context: Context,val viewModel :FavoriteViewModel) : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cityName = itemView.fav_city_name
        val favImg = itemView.fav_image
        val deleteBtn = itemView.btn_delete_fav
        val constrain = itemView.fav_constraint
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val recyclerViewItem =
            LayoutInflater.from(parent.context).inflate(R.layout.fav_row, parent, false)
        return MyViewHolder(recyclerViewItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cityName.text = getCityName(weatherModels[position].lat,weatherModels[position].lon)
        holder.deleteBtn.setOnClickListener(View.OnClickListener {

            viewModel.deleteData(weatherModels[position].timezone)
        })
        holder.constrain.setOnClickListener(View.OnClickListener {

            sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()

            editor.putString(lat, weatherModels[position].lat.toString())
            editor.putString(lon, weatherModels[position].lon.toString())
            Repository.editor.putString(timeZoneShared, weatherModels[position].timezone)
            editor.putString("map", "1")
            editor.apply()
            Navigation.findNavController(it).navigate(R.id.action_favoriteFragment_to_homeFragment)
        })


    }
    private fun getCityName(lat: Double, lon: Double): String {
        sharedPreferences = context.getSharedPreferences(com.example.weatherforecast.ui.activity.lat, Context.MODE_PRIVATE)
        var lang = sharedPreferences.getString("lang", "en")
        var city = ""
        val geocoder = Geocoder(context, Locale(lang))
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea // damietta
            city = "${state.split(" ")[0]}"
        }
        return city
    }

    override fun getItemCount(): Int {
        return weatherModels.size
    }
}