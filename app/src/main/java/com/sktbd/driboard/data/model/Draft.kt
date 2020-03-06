package com.sktbd.driboard.data.model

import android.provider.MediaStore
import java.util.ArrayList
import androidx.room.PrimaryKey

data class Draft (
    @PrimaryKey(autoGenerate = true)
    var draftID: Long = 0,
    var title:String?,
    var description:String?,
    var tags: ArrayList<String>?
)