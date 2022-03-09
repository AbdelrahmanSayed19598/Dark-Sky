package com.example.weatherforecast.homescreen.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.HoursPojo
import kotlinx.android.synthetic.main.hourly_weather_row.view.*

class HourlyAdapter (val hoursPojo: List<HoursPojo>): RecyclerView.Adapter<HourlyAdapter.MyViewHolder>() {
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
        holder.hoursTxt.text = hoursPojo[position].hours
        holder.imgHourly.setImageResource(hoursPojo[position].img)
        holder.tempratureTxt.text = hoursPojo[position].tempreture
    }

    override fun getItemCount(): Int {
        return hoursPojo.size
    }
}