package com.example.weatherforecast.ui.alarm.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.WeatherAlert
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.alarm.viewModel.AlertViewModel
import kotlinx.android.synthetic.main.alert_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertApadter(val alerts: List<WeatherAlert>, val context: Context,private val viewModel :AlertViewModel) :
    RecyclerView.Adapter<AlertApadter.MyViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var startTime = itemView.start_time!!
        var endtime = itemView.end_time!!
        var startDate = itemView.start_date!!
        var endDate = itemView.end_date!!
        var deleteBtn = itemView.delete_alert!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val recyclerViewItem =
            LayoutInflater.from(parent.context).inflate(R.layout.alert_row, parent, false)
        return MyViewHolder(recyclerViewItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.startTime.text = convertLongToTime(alerts[position].startTime)
        holder.endtime.text = convertLongToTime(alerts[position].endTime)
        holder.startDate.text = convertCalenderToDayDate(alerts[position].startDate)
        holder.endDate.text = convertCalenderToDayDate(alerts[position].endDate)

        holder.deleteBtn.setOnClickListener(View.OnClickListener {
                viewModel.deleteAlerts(alerts[position].id!!)
        })
        if(!checkTime(alerts[position])){
            viewModel.deleteAlerts(alerts[position].id!!)
        }

    }



    fun convertCalenderToDayDate(time: Long): String {
        sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)
        var lang = sharedPreferences.getString("lang", "en")
        val date = Date(time)
        val format = SimpleDateFormat("d MMM, yyyy", Locale(lang))
        return format.format(date)
    }


    fun convertLongToTime(time: Long): String {
        sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)
        var lang = sharedPreferences.getString("lang", "en")
        val date = Date(TimeUnit.SECONDS.toMillis(time))
        val format = SimpleDateFormat("h:mm a", Locale(lang))
        return format.format(date)
    }

    private fun checkTime(alert: WeatherAlert): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        return (dayNow in alert.startDate..alert.endDate)
    }
    private fun getDateMillis(date: String): Long {
        sharedPreferences = context.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
       var language = sharedPreferences.getString("lang", "en").toString()
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(language))
        val d: Date = f.parse(date)
        return d.time
    }

    override fun getItemCount(): Int {
        return alerts.size
    }
}