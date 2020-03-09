package com.sktbd.driboard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sktbd.driboard.data.model.DraftEntity

@Database(entities = arrayOf(DraftEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun draftDao(): DraftDao
}

