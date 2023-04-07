package com.example.weatherapplication.workers

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.setting.view.SettingFragment
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OneTimeWork(private val context: Context,
                  workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    var repo = WeatherRepo.getInstance(RemoteSourceImpl.getInstance(context), LocalSourceImpl(context),context)
    var notificationManager : NotificationManager? = null

    companion object {
        private val CHANNEL_ID = "channel_ID"
    }

    enum class Enum_sound(){alarm,notification}
    override suspend fun doWork(): Result {
        notificationChannel()

        val sharedPreference =
            context.getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        val alertString = inputData.getString("alert")
        var alert = Gson().fromJson(alertString, Alert::class.java)
        val currentTime = System.currentTimeMillis().div(1000)
        if (currentTime >= alert.startDay && currentTime <= alert.endDay) {
            val responseModel = repo.getCurrentTempData(alert.lat, alert.lon, "eng", "metric")
            var desc: String =
                responseModel.body()?.alerts?.get(0)?.event ?: context.getString(R.string.no_alarm)
            Log.i("mariam", "doWork: ${responseModel.body()} ")
            if (desc == "") desc = context.getString(R.string.no_alarm)
            if (sharedPreference.getString(
                    "alert",
                    Enum_sound.notification.toString()
                ) == Enum_sound.notification.toString()
            ) {
                makeNotification(desc)
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    AlertWindow(context, desc, alert.AlertCityName).onCreate()
                }
            }
            return Result.success()
        } else
            if (currentTime > alert.endDay) {
                //var appDatabase = AppDatabase.getInstance(context)
                var localSourceImpl = LocalSourceImpl.getInstance(context)
                GlobalScope.launch(Dispatchers.Main) {
                    localSourceImpl.deleteAlerts(alert)
                }
                WorkManager.getInstance(context)
                    .cancelAllWorkByTag(alert.startDay.toString() + alert.startDay.toString())
                return Result.success()
            } else {
                return Result.failure()

                return Result.success()
            }
    }

//    private fun makeNotification(desc: String,title:String) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "channel_name"
//            val descriptionText = "channel_description"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager =applicationContext
//                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
//            .setSmallIcon(R.drawable.cloud_icon)
//            .setContentTitle(title)
//            .setContentText(desc)
//            .setSound(alarmSound)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        with(NotificationManagerCompat.from(applicationContext)) {
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return
//            }
//            notify(1, builder.build())
//        }
//
//    }
@RequiresApi(Build.VERSION_CODES.O)
private fun makeNotification(description: String){
    Log.e("MyOneTimeWorkManger","makeNotification")
    lateinit var builder: Notification.Builder

    val intent = Intent(applicationContext, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    builder= Notification.Builder(applicationContext, "$CHANNEL_ID")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(description)
        .setContentTitle("mariam")
        .setPriority(Notification.PRIORITY_DEFAULT)
        .setStyle(
            Notification.BigTextStyle()
                .bigText(description)
        )
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .setLights(Color.RED, 3000, 3000)
        .setAutoCancel(true)
    notificationManager?.notify(1234, builder.build())

}

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              val name = "channel_name"
            val channel = NotificationChannel("$CHANNEL_ID", name, NotificationManager.IMPORTANCE_DEFAULT)
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.enableVibration(true)
            channel.description = "desc"
            notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            Log.e("MyOneTimeWorkManger","notificationChannel")

        }
    }
}