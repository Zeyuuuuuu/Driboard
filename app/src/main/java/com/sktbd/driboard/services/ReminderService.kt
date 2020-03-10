package com.sktbd.driboard.services

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.sktbd.driboard.R
import com.sktbd.driboard.data.db.AppDatabase
import com.sktbd.driboard.ui.activity.MainActivity


class ReminderService : IntentService("ReminderService") {
    private val NOTIFICATION_ID = 5453
    private val NOTIFICATION_CHANNEL_ID = "my_channel_01"
    private val NOTIFICATION_CHANNEL_NAME = "my_channel"

    override fun onHandleIntent(intent: Intent?) {
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
                Log.i("Service", "YES")
                sendNotification()
            }
        }
    }

    private fun createNotificationChannel() {

    }

    private fun sendNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "my_channel_01"
            val name: CharSequence = "my_channel"
            val Description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = Description
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(mChannel)
        }
        val notification = NotificationCompat.Builder(this)
            .setContentTitle("You have drafts")
            .setContentText("You have unpublished drafts")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_background)
        //Create an action
        val actionIntent = Intent(this, MainActivity::class.java)
        val actionPendingIntent =
            PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notification.setContentIntent(actionPendingIntent)
        notificationManager.notify(NOTIFICATION_ID, notification.build()
        )
    }
}