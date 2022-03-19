package com.example.weatherforecast.workManager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.weatherforecast.R
import com.example.weatherforecast.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*

class AlertService : Service() {

    val CHANNEL_ID = 1
    val FOREGROUND_ID = 7
    var notificationManager: NotificationManager? = null
    override fun onBind(p0: Intent?): IBinder? {

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var description = intent?.getStringExtra("description")
        var icon = intent?.getStringExtra("icon")

        notificationChannel()
        startForeground(FOREGROUND_ID, makeNotification(description!!, icon!!))
        return START_NOT_STICKY
    }

    private fun makeNotification(description: String, icon: String): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bitmap = BitmapFactory.decodeResource(resources, getIcon(icon))

        return NotificationCompat.Builder(
            applicationContext, "$CHANNEL_ID"
        )
            .setSmallIcon(getIcon(icon))
            .setContentText(description)
            .setContentTitle("Weather Alarm")
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(description)
//            )
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
            .build()
    }

    private fun getIcon(icon: String): Int {
        when (icon) {

            "01d" -> return R.drawable.ic_sun_svgrepo_com
            "01n" -> return R.drawable.ic_moon_svgrepo_com
            "02d" -> return R.drawable.twod
            "02n" -> return R.drawable.twon
            "03d" -> return R.drawable.threed
            "03n" -> return R.drawable.threen
            "04d" -> return R.drawable.fourd
            "04n" -> return R.drawable.fourn
            "09d" -> return R.drawable.nined
            "09n" -> return R.drawable.ninen
            "10d" -> return R.drawable.tend
            "10n" -> return R.drawable.tenn
            "11d" -> return R.drawable.eleven_d
            "11n" -> return R.drawable.eleven_n
            "13d" -> return R.drawable.ic_snow_svgrepo_com
            "13n" -> return R.drawable.ic_snow_svgrepo_com
            "50d" -> return R.drawable.fifty_d
            "50n" -> return R.drawable.fifty_n
            else -> return R.drawable.ic_sun_svgrepo_com
        }

    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                "$CHANNEL_ID",
                name, importance
            )
            channel.description = description
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}