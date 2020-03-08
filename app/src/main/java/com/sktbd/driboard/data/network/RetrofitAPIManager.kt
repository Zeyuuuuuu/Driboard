package com.sktbd.driboard.data.network

import com.sktbd.driboard.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class RetrofitAPIManager {
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

    fun genericClient(): OkHttpClient? {
        return OkHttpClient.Builder()
            .addInterceptor{chain ->
                    val request: Request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", Constants.ACCESS_TOKEN)
                        .build()
                    chain.proceed(request)
                }
            .build()
    }

}