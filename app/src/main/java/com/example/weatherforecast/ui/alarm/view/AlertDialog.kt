package com.example.weatherforecast.ui.alarm.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.work.*
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.data.model.WeatherAlert
import com.example.weatherforecast.ui.activity.MainActivity
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.activity.timeZoneShared
import com.example.weatherforecast.ui.alarm.viewModel.DialogAlertsViewModel
import com.example.weatherforecast.ui.alarm.viewModel.DialogAlertsViewModelFactory
import com.example.weatherforecast.workManager.AlertPeriodicWorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_alert_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AlertDialog : DialogFragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var language: String
    private lateinit var weatherAlert: WeatherAlert
    private val viewModel: DialogAlertsViewModel by viewModels {
        DialogAlertsViewModelFactory(Repository.getRepoInstance(requireActivity().application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        language = sharedPreferences.getString("lang", "en").toString()
        val calendar = Calendar.getInstance().timeInMillis



        setCardsInitialText(calendar)

        btn_From.setOnClickListener(View.OnClickListener {
            showDatePicker(true)
        })
        btn_to.setOnClickListener(View.OnClickListener {
            showDatePicker(false)
        })


        btn_save.setOnClickListener(View.OnClickListener {
            viewModel.insertAlerts(weatherAlert)
            viewModel.id.observe(viewLifecycleOwner) {
                callPeriodicWorkManager(it.toInt())
            }
            dialog?.dismiss()
        })


    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setCardsInitialText(current: Long) {
        val timeNow = convertLongToTime(current / 1000L, language)
        val currentDate = convertCalenderToDayDate(current, language)
        val oneHour: Long = 1000 * (3600L)
        val afterOneHour = current + oneHour
        val timeAfterOneHour = convertLongToTime(afterOneHour / 1000L, language)
        Log.i("as", "setCardsInitialText: "+timeNow + " "+timeAfterOneHour)
        btn_From.text = currentDate.plus("\n").plus(timeNow)
        btn_to.text = currentDate.plus("\n").plus(timeAfterOneHour)
        weatherAlert = WeatherAlert(null, current / 1000, afterOneHour / 1000, current, current)
    }

    private fun showTimePicker(isFrom: Boolean, datePicker: Long) {
        Locale.setDefault(Locale(language))
        val currentHour = Calendar.HOUR_OF_DAY
        val currentMinute = Calendar.MINUTE
        val listener: (TimePicker?, Int, Int) -> Unit =
            { _: TimePicker?, hour: Int, minute: Int ->
                val time = TimeUnit.MINUTES.toSeconds(minute.toLong()) +
                        TimeUnit.HOURS.toSeconds(hour.toLong()) - (3600L * 2)
                val dateString = convertCalenderToDayDate(datePicker, language)
                val timeString = convertLongToTime(time, language)
                val text = dateString.plus("\n").plus(timeString)
                if (isFrom) {
                    btn_From.text = text
                    weatherAlert.startTime = time
                } else {
                    btn_to.text = text
                    weatherAlert.endTime = time
                }
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            listener, currentHour, currentMinute, false
        )

        timePickerDialog.setTitle(getString(R.string.time_picker))
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun showDatePicker(isFrom: Boolean) {
        Locale.setDefault(Locale(language))
        val myCalender = Calendar.getInstance()
        val year = myCalender[Calendar.YEAR]
        val month = myCalender[Calendar.MONTH]
        val day = myCalender[Calendar.DAY_OF_MONTH]
        val myDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->

                if (view.isShown) {
                    val date = "$day/${month + 1}/$year"
                    if (isFrom) {
                        weatherAlert.startDate = getDateMillis(date)
                    } else {
                        weatherAlert.endDate = getDateMillis(date)
                    }
                    showTimePicker(isFrom, getDateMillis(date))

                }

            }
        val datePickerDialog = DatePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myDateListener, year, month, day
        )
        datePickerDialog.setTitle(getString(R.string.date_picker))
        datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }

    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(language))
        val d: Date = f.parse(date)
        return d.time
    }

    fun convertCalenderToDayDate(time: Long, language: String): String {
        val date = Date(time)
        val format = SimpleDateFormat("d MMM, yyyy", Locale(language))
        return format.format(date)
    }


    fun convertLongToTime(time: Long, language: String): String {
        val date = Date(TimeUnit.SECONDS.toMillis(time))
        val format = SimpleDateFormat("h:mm a", Locale(language))
        return format.format(date)
    }

    private fun callPeriodicWorkManager(id: Int) {
        val shared = initSharedPref(requireContext())
        val timeZone = shared.getString(timeZoneShared, "null")
        val data = Data.Builder()
            .putString(timeZoneShared, timeZone)
            .putInt("ID", id).build()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            AlertPeriodicWorkManager::class.java,
            24,TimeUnit.HOURS
        )
            .setInputData(data)
            .setConstraints(constraints)
            .addTag(id.toString())
            .build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            id.toString(),ExistingPeriodicWorkPolicy.REPLACE
            ,periodicWorkRequest
        )

    }




    private fun initSharedPref(requireContext: Context): SharedPreferences {
        return requireContext.getSharedPreferences(lat, Context.MODE_PRIVATE)
    }


}