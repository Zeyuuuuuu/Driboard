package com.sktbd.driboard.data.network

import com.sktbd.driboard.data.model.Shot
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Path


interface DribbbleService {
    @GET ("user/shots")
    fun getShots(@Query("access_token") access_token:String): Call<List<Shot>>
    @GET ("shots/{id}")
    fun getShotById(@Path("id") id: Int, @Query("access_token") access_token:String): Call<Shot>
}