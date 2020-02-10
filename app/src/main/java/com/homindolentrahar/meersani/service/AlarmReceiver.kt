package com.homindolentrahar.meersani.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.ui.main.MainActivity
import com.homindolentrahar.meersani.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_DAILY_REMINDER = "Daily Reminder"
        const val TYPE_TODAY_RELEASE = "Today Released Movies"
        const val NOTIF_ID_DAILY_REMINDER = 101
        const val NOTIF_ID_TODAY_RELEASE = 202
        const val CHANNEL_ID = "MeersaniID"
        const val CHANNEL_NAME = "Meersani"
    }

    private val TAG = AlarmReceiver::class.java.simpleName
    private val apiService = Constants.getAPIService()

    @SuppressLint("CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())
        val listTitle = mutableListOf<String>()
        if (type == TYPE_DAILY_REMINDER) {
            showNotif(
                context,
                TYPE_DAILY_REMINDER, "Check out newest Movies and Series",
                NOTIF_ID_DAILY_REMINDER
            )
        } else {
            apiService.getTodayReleasedMovies(BuildConfig.API_KEY, currentDate, currentDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        response.results.forEach { item ->
                            listTitle.add(item.title)
                        }
                        Log.d(TAG, "Today Released Movies : ${response.results.size}")
                    },
                    { error ->
                        Log.d(TAG, "Error : ${error.message}")
                    }
                )
            val moviesTitle = TextUtils.join(",", listTitle)

            showNotif(
                context,
                TYPE_TODAY_RELEASE, moviesTitle,
                NOTIF_ID_TODAY_RELEASE
            )
        }
    }

    fun setupDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIF_ID_DAILY_REMINDER, getIntent(
                context,
                TYPE_DAILY_REMINDER
            ), 0
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            getReminderTime(TYPE_DAILY_REMINDER).timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Daily Reminder enabled !", Toast.LENGTH_SHORT).show()
    }

    fun setupTodayRelease(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIF_ID_TODAY_RELEASE, getIntent(
                context,
                TYPE_TODAY_RELEASE
            ), 0
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            getReminderTime(TYPE_TODAY_RELEASE).timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Today Released enabled !", Toast.LENGTH_SHORT).show()
    }

    fun cancelReminder(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode =
            if (type == TYPE_DAILY_REMINDER) NOTIF_ID_DAILY_REMINDER else NOTIF_ID_TODAY_RELEASE
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "$type alarm cancelled !", Toast.LENGTH_SHORT).show()
    }

    private fun getIntent(context: Context, type: String): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)
        return intent
    }

    private fun getReminderTime(type: String): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, if (type == TYPE_DAILY_REMINDER) 7 else 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (System.currentTimeMillis() > calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar
    }

    private fun showNotif(context: Context, title: String, message: String, notifId: Int) {
        val notifManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notifManager.createNotificationChannel(channel)
        }

        notifManager.notify(notifId, builder.build())
    }
}
