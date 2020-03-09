package com.sktbd.driboard.data.network

import android.util.Log
import com.sktbd.driboard.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitAPIManager(accessToken:String?) {
    private var token = Constants.ACCESS_TOKEN
    init{
        if (accessToken!=null)
            token = accessToken
    }
    fun getDriboardService():DriboardService{
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(genericClient())
            .build()
        return retrofit.create(DriboardService::class.java)
    }

    fun getAuthService():AuthService{
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.OAUTH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(genericClient())
                .build()
        return retrofit.create(AuthService::class.java)
    }

    private fun genericClient(): OkHttpClient {
        Log.i("token",token)
        return OkHttpClient.Builder()
            .addInterceptor{chain ->
                    val request: Request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(request)
                }
            .build()
    }

}