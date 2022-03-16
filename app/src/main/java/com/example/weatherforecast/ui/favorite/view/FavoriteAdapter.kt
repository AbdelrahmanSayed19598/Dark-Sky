package com.example.weatherforecast.ui.favorite.view

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Daily
import com.example.weatherforecast.data.model.WeatherModel
import com.example.weatherforecast.lat
import com.example.weatherforecast.lon
import com.example.weatherforecast.ui.view.adapter.DailyAdapter
import kotlinx.android.synthetic.main.daily_weather_row.view.*
import kotlinx.android.synthetic.main.fav_row.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class FavoriteAdapter(val weatherModels: List<WeatherModel>,val context: Context) : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {
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
        holder.cityName.text = weatherModels[position].timezone.toString()
        holder.deleteBtn.setOnClickListener(View.OnClickListener {

        })
        holder.constrain.setOnClickListener(View.OnClickListener {
            sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()

            editor.putString(lat, weatherModels[position].lat.toString())
            editor.putString(lon, weatherModels[position].lon.toString())
            editor.putString("map", "1")
            editor.apply()
            Navigation.findNavController(it).navigate(R.id.action_favoriteFragment_to_homeFragment)
        })


    }


    override fun getItemCount(): Int {
        return weatherModels.size
    }
}