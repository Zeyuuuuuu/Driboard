package com.sktbd.driboard.data.network

import com.sktbd.driboard.data.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface UserApiService {
    @GET("user/")
    fun getUser(@Query("access_token") access_token:String): Call<User>
}