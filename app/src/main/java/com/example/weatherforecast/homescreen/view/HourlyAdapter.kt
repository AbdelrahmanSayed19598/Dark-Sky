package com.example.weatherforecast.homescreen.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.response.Hourly
import com.example.weatherforecast.model.HoursPojo
import kotlinx.android.synthetic.main.hourly_weather_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter (val hoursPojo: List<Hourly>): RecyclerView.Adapter<HourlyAdapter.MyViewHolder>() {
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
       // holder.imgHourly.setImageResource(hoursPojo[position].img)
        holder.tempratureTxt.text = (hoursPojo[position].temp.toInt()).toString()+"°c"
    }

    private fun timeFormat(millisSeconds:Int ): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }

    override fun getItemCount(): Int {
        return hoursPojo.size
    }
}