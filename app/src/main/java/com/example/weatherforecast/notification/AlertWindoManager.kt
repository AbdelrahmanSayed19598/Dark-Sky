package com.example.weatherforecast.notification

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.AlerWindoNetoficationBinding

class AlertWindoManager(private val context: Context,private val icon:Int,private val description:String) {
    private var windowManager: WindowManager? = null
    private var customNotificationDialogView: View? = null
    private var binding: AlerWindoNetoficationBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    fun setWindowManger() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customNotificationDialogView =
            inflater.inflate(R.layout.aler_windo_netofication, null)
        binding = AlerWindoNetoficationBinding.bind(customNotificationDialogView!!)
        bindViews()
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
        windowManager!!.addView(customNotificationDialogView, params)
    }



    private fun bindViews() {
        // must contain email
        sharedPreferences = context.getSharedPreferences(com.example.weatherforecast.ui.activity.lat, Context.MODE_PRIVATE)
        var lang = sharedPreferences.getString("lang", "en")
        binding?.alertImage?.setImageResource(icon)
        when(lang){
           "en"->{ binding?.description?.text = "the weather now is $description"}
           "ar"->{ binding?.description?.text = "الطقس الحالي $description"}
        }

        binding?.okBtn?.setOnClickListener {
            stopMyService()
            close()
        }
    }


    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customNotificationDialogView
            )
            customNotificationDialogView!!.invalidate()
            (customNotificationDialogView!!.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
        }
    }

    private fun stopMyService() {
        context.stopService(Intent(context, AlertService::class.java))
    }

}