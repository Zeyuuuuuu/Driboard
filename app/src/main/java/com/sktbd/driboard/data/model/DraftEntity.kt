package com.sktbd.driboard.data.model

import android.provider.MediaStore
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import java.util.ArrayList
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "drafts")
data class DraftEntity (
    @PrimaryKey var draftID: Long = 0,
    var id:String,
    var title:String,
    var description:String,
    var tags: String,
    var imageUri:String)


