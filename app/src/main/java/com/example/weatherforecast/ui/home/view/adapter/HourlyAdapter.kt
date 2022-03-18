package com.example.weatherforecast.ui.home.view.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Hourly
import com.example.weatherforecast.ui.activity.lat
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
        sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)
        var lang = sharedPreferences.getString("lang", "en").toString()
        var unit =sharedPreferences.getString("unit", "metric")

        holder.hoursTxt.text = timeFormat( hoursPojo[position].dt.toInt(),lang)


        when(unit){

            "imperial" ->  holder.tempratureTxt.text =  arabicToEnglish(hoursPojo[position].temp.toInt().toString(),lang)+"°f"
            "metric" -> holder.tempratureTxt.text = arabicToEnglish(hoursPojo[position].temp.toInt().toString(),lang)+"°c"
            "standard" -> holder.tempratureTxt.text = arabicToEnglish(hoursPojo[position].temp.toInt().toString(),lang)+"°k"
        }

        when(hoursPojo[position].weather.get(0).icon){

            "01d"-> holder.imgHourly.setImageResource(R.drawable.ic_sun_svgrepo_com)
            "01n"-> holder.imgHourly.setImageResource(R.drawable.ic_moon_svgrepo_com)
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
            "13d"-> holder.imgHourly.setImageResource(R.drawable.ic_snow_svgrepo_com)
            "13n"-> holder.imgHourly.setImageResource(R.drawable.ic_snow_svgrepo_com)
            "50d"-> holder.imgHourly.setImageResource(R.drawable.fifty_d)
            "50n"-> holder.imgHourly.setImageResource(R.drawable.fifty_n)
        }
    }

    private fun timeFormat(millisSeconds: Int, lang: String?): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        val format = SimpleDateFormat("hh:00 aaa", Locale(lang))
        return format.format(calendar.time)
    }

    override fun getItemCount(): Int {
        return hoursPojo.size
    }
    fun arabicToEnglish(str: String, lang:String):String {

        if(lang.equals("ar")) {
            var result = ""
            var en = '0'
            for (ch in str) {
                en = ch
                when (ch) {
                    '0' -> en = '۰'
                    '1' -> en = '۱'
                    '2' -> en = '۲'
                    '3' -> en = '۳'
                    '4' -> en = '٤'
                    '5' -> en = '۵'
                    '6' -> en = '٦'
                    '7' -> en = '۷'
                    '8' -> en = '۸'
                    '9' -> en = '۹'
                }
                result = "${result}$en"
            }
            return result
        }else{
            return str
        }
    }
}