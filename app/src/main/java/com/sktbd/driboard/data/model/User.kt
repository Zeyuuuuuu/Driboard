package com.sktbd.driboard.data.model

import com.google.gson.annotations.SerializedName
import com.sktbd.driboard.utils.Constants.EMPTY_STRING

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
                @SerializedName("followers_count")
                val followersCount:Int){
    data class Links(val web:String?, val twitter:String?)
}
