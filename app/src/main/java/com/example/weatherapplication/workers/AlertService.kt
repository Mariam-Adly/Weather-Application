package com.example.weatherapplication.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R

class AlertService : Service() {
    private var alertDescription: String = ""
    private var title =""
    private var cityNmae = ""
    private lateinit var notificationManager: NotificationManager
    private var icon = 0

    private var isAlert: Boolean = false
    val CHANNEL_ID = 123
    val FOREGROUND_ID = 10
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        createNotificationChannel()
        startForeground(
            FOREGROUND_ID,
            makeNotification()
        )

        alertDescription = intent.getStringExtra("desc")!!
        cityNmae = intent.getStringExtra("city")!!
        isAlert = intent.getBooleanExtra(getString(R.string.alert), false)

        if (isAlert) {
            icon = R.drawable.ic_warning_com
            alertDescription = "You should be careful".plus(".").plus("\n")
                .plus(getString(R.string.the_weather_is)).plus(alertDescription)

        } else {
            icon = R.drawable.ic_sunrise
            alertDescription = getString(R.string.ther_is_no_alarm).plus(".").plus("\n")
                .plus(getString(R.string.the_weather_is)).plus("  ").plus(alertDescription)
        }
        startForeground(
            FOREGROUND_ID,
            makeNotification()
        )
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(this.applicationContext)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            // call window manager
            val myWorkManager = AlertWindow(
                this, alertDescription,cityNmae)
            myWorkManager.onCreate()
        }

        return START_NOT_STICKY

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeNotification(): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        return NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID.toString()
        )
            .setSmallIcon(icon)

            .setContentTitle(getString(R.string.weather_alert))
            .setContentText(alertDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true).build()
    }

    private fun createNotificationChannel() {
        val sound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.summer) //Here is FILE_NAME is the name of file that you want to play
        var attributes =  AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID.toString(),
                name, importance
            )
            channel.description = alertDescription
            channel.setSound(sound, attributes); //
            channel.enableVibration(true)

            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


}