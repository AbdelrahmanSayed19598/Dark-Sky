package com.example.weatherforecast.ui.view.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Hourly
import com.example.weatherforecast.lat
import kotlinx.android.synthetic.main.hourly_weather_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter (val hoursPojo: List<Hourly>,val context: Context): RecyclerView.Adapter<HourlyAdapter.MyViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val hoursTxt =itemView.txtHours!!
        val imgHourly =itemView.imgHourly!!
        val tempratureTxt = itemView.txtTempreture!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val recyclerViewItem = LayoutInflater.from(parent.context).inflate(R.layout.hourly_weather_row,parent,false)
        return MyViewHolder(recyclerViewItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hoursTxt.text = timeFormat( hoursPojo[position].dt.toInt())
        holder.tempratureTxt.text = (hoursPojo[position].temp.toInt()).toString()+"°c"


        when(hoursPojo[position].weather.get(0).icon){

            "01d"-> holder.imgHourly.setImageResource(R.drawable.oned)
            "01n"-> holder.imgHourly.setImageResource(R.drawable.onen)
            "02d"-> holder.imgHourly.setImageResource(R.drawable.twod)
            "02n"-> holder.imgHourly.setImageResource(R.drawable.twon)
            "03d"-> holder.imgHourly.setImageResource(R.drawable.threed)
            "03n"-> holder.imgHourly.setImageResource(R.drawable.threen)
            "04d"-> holder.imgHourly.setImageResource(R.drawable.fourd)
            "04n"-> holder.imgHourly.setImageResource(R.drawable.fourn)
            "09d"-> holder.imgHourly.setImageResource(R.drawable.nined)
            "09n"-> holder.imgHourly.setImageResource(R.drawable.ninen)
            "10d"-> holder.imgHourly.setImageResource(R.drawable.tend)
            "10n"-> holder.imgHourly.setImageResource(R.drawable.tenn)
            "11d"-> holder.imgHourly.setImageResource(R.drawable.eleven_d)
            "11n"-> holder.imgHourly.setImageResource(R.drawable.eleven_n)
            "13d"-> holder.imgHourly.setImageResource(R.drawable.thirteen_d)
            "13n"-> holder.imgHourly.setImageResource(R.drawable.thirteen_n)
            "50d"-> holder.imgHourly.setImageResource(R.drawable.fifty_d)
            "50n"-> holder.imgHourly.setImageResource(R.drawable.fifty_n)
        }
    }

    private fun timeFormat(millisSeconds:Int ): String {
        sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)
        var lang = sharedPreferences.getString("lang", "en")

        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        val format = SimpleDateFormat("hh:00 aaa", Locale(lang))
        return format.format(calendar.time)
    }

    override fun getItemCount(): Int {
        return hoursPojo.size
    }
}