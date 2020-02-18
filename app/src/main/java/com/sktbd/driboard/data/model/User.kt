package com.sktbd.driboard.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(val id: Int,
                @SerializedName("login")
                val userName: String,
                val name: String?,
                @SerializedName("html_url")
                val htmlUrl: String?,
                @SerializedName("avatar_url")
                val avatarUrl:String,
                val location: String?,
                val links:Links?,
                val bio:String?,
                @SerializedName("followers_count")
                val followersCount:Int): Serializable{

    data class Links(val web:String?,
                     val twitter:String?): Serializable
}
