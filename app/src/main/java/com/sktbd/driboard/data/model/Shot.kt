package com.sktbd.driboard.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Shot(val id: Int,
                @SerializedName("images")
                val links:ImgLinks,
                val description: String,
                val height: Int,
                val width: Int,
                val title: String,
                val tags: List<String>,
                @SerializedName("html_url")
                val htmlUrl: String
                ): Serializable{
    data class ImgLinks(val hidpi:String):Serializable
}