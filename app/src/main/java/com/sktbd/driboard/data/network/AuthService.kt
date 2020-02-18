package com.sktbd.driboard.data.network

import com.sktbd.driboard.data.model.AuthToken
import retrofit2.Call

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("token")
    fun getAccessToken(@Field("client_id") client_id: String,
                       @Field("client_secret") client_secret: String,
                       @Field("code") code: String,
                       @Field("redirect_uri") redirect_uri: String):
            Call<AuthToken>
}