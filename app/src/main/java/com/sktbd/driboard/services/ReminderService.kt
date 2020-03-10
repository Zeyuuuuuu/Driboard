package com.sktbd.driboard.services

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.sktbd.driboard.R
import com.sktbd.driboard.data.db.AppDatabase


class ReminderService : IntentService("ReminderService") {


    override fun onHandleIntent(intent: Intent?) {

        createNotificationChannel()

        var builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.baseline_add_black_24dp)
            .setContentTitle("Do you forget your drafts？")
            .setContentText("There're still drafts haven't been published！")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        var db = Room.databaseBuilder(this, AppDatabase::class.java, "drafts").allowMainThreadQueries().build()
        Log.i("Service","Start")
        while (true) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }
            if (db.draftDao().getAll().size > 0){
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(System.currentTimeMillis().toInt(), builder.build())
                }
                Log.i("Service", "YES")
            }
        }

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}