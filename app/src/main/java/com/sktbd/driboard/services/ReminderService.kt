package com.sktbd.driboard.services

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.room.Room
import com.sktbd.driboard.data.db.AppDatabase


class ReminderService : IntentService("ReminderService") {


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
            }
        }


    }
}