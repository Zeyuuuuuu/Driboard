package com.sktbd.driboard.data.network

import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DriboardService {
    @GET("user/")
    fun getUser(@Query("access_token") access_token:String): Call<User>
    @GET("user/shots")
    fun getUserShots(@Query("access_token") access_token:String): Call<List<Shot>>
}