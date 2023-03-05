package com.example.newsapp.ui



import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.newsapp.R
import java.util.*


class Notification : Service() {
        private val CHANNEL_ID = "NotificationService"
        private val NOTIFICATION_ID = 1
        private val INTERVAL = 6*60*60 * 1000 // 6 hours in milliseconds
        private val GROUP_ID = "MyNotificationGroup"
        private lateinit var alarmManager: AlarmManager
        private lateinit var pendingIntent: PendingIntent

        override fun onBind(intent: Intent?): Nothing? = null


        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate() {
            super.onCreate()

            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, Notification::class.java)
            pendingIntent = PendingIntent.getService(applicationContext, NOTIFICATION_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            scheduleNotification()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            scheduleNotification()
            return START_STICKY
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun scheduleNotification() {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                add(Calendar.MILLISECOND, INTERVAL)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    INTERVAL.toLong(),
                    pendingIntent
                )
            }

            showNotification()
        }

        override fun onDestroy() {
            super.onDestroy()
            alarmManager.cancel(pendingIntent)

        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun showNotification():android.app.Notification {
            createNotificationChannel()

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_news)
                .setOngoing(false)
                .setContentTitle("News APP")
                .setContentText("Не хотите взглянуть на самые актуальные новости за последние 6 часов")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(GROUP_ID)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, builder.build())


            return builder.build()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel() {
            val name = "Notification Channel"
            val descriptionText = "Notification Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val group = NotificationChannelGroup(GROUP_ID, "My Notification Group")
            notificationManager.createNotificationChannelGroup(group)
        }


}