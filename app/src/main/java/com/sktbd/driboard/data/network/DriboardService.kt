package com.sktbd.driboard.data.network

import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.ArrayList

interface DriboardService {
    @GET("user/")
    fun getUser(@Query("access_token") access_token:String): Call<User>
    @GET("user/shots")
    fun getUserShots(@Query("access_token") access_token:String): Call<List<Shot>>


    @FormUrlEncoded
    @PUT("shots/{id}")
    fun editShot(
        @Path(value = "id", encoded = true) id: String?,
        @Field("title") title: String?,
        @Field("description") description: String,
        @Field("tags[]") tags: ArrayList<String>,
        @Field("low_profile") isLowProfile: Boolean
        //@Field("scheduled_for") Date publishDate,
        //@Field("teamID") int teamID
    ): Call<Response<Void>>

    @Multipart
    @POST("shots/")
    fun publishShot(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>, //See : https://stackoverflow.com/a/40873297
        @Part file: MultipartBody.Part,
        @Part("title") title: String?,
        @Part("description") description: String?,
        @Part("tags[]") tags: List<String>?
    ): Call<Response<Void>>
}