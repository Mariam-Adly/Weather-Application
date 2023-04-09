package com.example.weatherapplication.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.datasource.database.LocalSourceImpl
import com.example.weatherapplication.datasource.network.RemoteSourceImpl
import com.example.weatherapplication.datasource.repo.WeatherRepo
import com.example.weatherapplication.model.Alert
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit


class OneTimeWork(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    var repo = WeatherRepo.getInstance(
        RemoteSourceImpl.getInstance(context),
        LocalSourceImpl(context),
        context
    )
    var notificationManager: NotificationManager? = null

    companion object {
        private val CHANNEL_ID = "channel_ID"


        fun setWorker(myAlert: Alert, context: Context) {
            //prepare the time for next job to run
            val now = Calendar.getInstance().timeInMillis
            Calendar.getInstance().timeInMillis
            val time = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(myAlert.Time * 1000),
                ZoneId.systemDefault()
            )
            val dateTimeStart = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(myAlert.startDay * 1000),
                ZoneId.systemDefault()
            ).withMinute(time.minute).withHour(time.hour).withSecond(time.second)
            val diffInMillis =
                dateTimeStart.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli() - now
            val timeDifferenceSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
            Log.i("mariam", "dateTimeStart: $dateTimeStart")
            Log.i("mariam", "dateTimeNow: ${Date(now)}")
            Log.i("mariam", "timeDifferenceSeconds: $timeDifferenceSeconds")
            //send the data to the work manager job
            val data = Data.Builder()
            var stringAlert = Gson().toJson(myAlert)
            data.putString("alert", stringAlert)
            //schedule the work task
            val workRequest = PeriodicWorkRequestBuilder<OneTimeWork>(1, TimeUnit.DAYS)
                .setInitialDelay(timeDifferenceSeconds, TimeUnit.SECONDS)
                .addTag(myAlert.startDay.toString() + myAlert.endDay.toString())
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                myAlert.startDay.toString() + myAlert.endDay.toString(),
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }

    }

    enum class Enum_sound() { alarm, notification }

    override suspend fun doWork(): Result {
        Log.d("mariam", "doWork: ")

        notificationChannel()
        val sharedPreference =
            context.getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        val alertString = inputData.getString("alert")
        Log.d("mariam", "alertString: $alertString")
        var alert = Gson().fromJson(alertString, Alert::class.java)
        val now = Calendar.getInstance().timeInMillis
//        if ((now > alert.endDay* 1000)) {
//            //cancel the work task
//            WorkManager.getInstance(context).cancelUniqueWork(alert.startDay.toString() + alert.endDay.toString())
//            Log.d("mariam", "cancelUniqueWork: ")
//            return Result.success()
//        }
        val currentTime = System.currentTimeMillis().div(1000)
        val responseModel = repo.getCurrentTempData(alert.lat, alert.lon, "eng", "metric")
        val openWeather = responseModel.body()
        val notificationDescription =
            if (!openWeather?.alerts.isNullOrEmpty() && openWeather?.alerts!![0].event != null) {
                responseModel.body()?.alerts?.get(0)!!.event!!
            } else {
                openWeather?.current?.weather?.get(0)?.description
                    ?: context.getString(R.string.no_alarm)
            }
        val alertType = sharedPreference.getString("alert", Enum_sound.notification.toString())
        if (alertType == Enum_sound.notification.toString()
        ) {
            makeNotification(notificationDescription, alert.AlertCityName)
        } else {
            val intent = Intent(context, AlertService::class.java).apply {
                putExtra("desc",notificationDescription)
                putExtra("city",alert.AlertCityName)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(applicationContext, intent)
            } else {
                applicationContext.startService(intent)
            }
           // AlertWindow(context, notificationDescription, alert.AlertCityName).onCreate()
        }
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeNotification(description: String, cityName: String) {
        Log.e("MyOneTimeWorkManger", "makeNotification")
        lateinit var builder: Notification.Builder

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder = Notification.Builder(applicationContext, "$CHANNEL_ID")
            .setSmallIcon(R.drawable.cloud_icon)
            .setContentText(description)
            .setSound(alarmSound)
            .setContentTitle(cityName)
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
            val channel =
                NotificationChannel("$CHANNEL_ID", name, NotificationManager.IMPORTANCE_DEFAULT)
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.enableVibration(true)
            channel.description = "desc"
            notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            Log.e("MyOneTimeWorkManger", "notificationChannel")

        }
    }
}