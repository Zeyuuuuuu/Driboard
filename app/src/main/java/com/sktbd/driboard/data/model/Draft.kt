package com.sktbd.driboard.data.model

import java.util.ArrayList
import androidx.room.PrimaryKey
import java.io.Serializable

data class Draft (
    @PrimaryKey(autoGenerate = true)
    var draftID: Long = 0,
    var id:String,
    var title:String,
    var description:String?,
    var tags: ArrayList<String>?,
    var images:ImageUrl?,
    var imageUri:String?)
    : Serializable {
    data class ImageUrl(
        var normal:String
    )
}