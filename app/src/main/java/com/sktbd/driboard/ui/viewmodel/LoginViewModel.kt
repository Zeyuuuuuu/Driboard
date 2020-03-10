package com.sktbd.driboard.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sktbd.driboard.data.model.AuthToken
import com.sktbd.driboard.data.network.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginViewModel: ViewModel() {

    private val BASE_URL = "https://dribbble.com/oauth/"
    private val CLIENT_ID: String = "776e7c6224c49228e619abeaba2c967e84f3d0360865de17d61b1e653759fd1f"
    private val CLIENT_SECRET: String = "ec8ea1868ccebd63dd334bf5985f432ce7ce6e8ed5e0537c242ea0f28f5cc03b"
    private val REDIRECT_URI: String = "driboard://callback"

    private val retrofit: Retrofit
    private val authService: AuthService
    val accessToken: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        authService = retrofit.create(AuthService::class.java)
        accessToken.value = ""

        Log.i("LoginViewModel", "LoginViewModel Created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "LoginViewModel Destroyed")
    }


    fun getAccessToken(code: String) {
        var call = authService.getAccessToken(CLIENT_ID, CLIENT_SECRET, code, REDIRECT_URI)
        call.enqueue(object : Callback<AuthToken> {
            override fun onFailure(call: Call<AuthToken>, t: Throwable) {
                Log.e("getAccessToken", "Error in getAccessToken")
            }

            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {
                Log.i("LoginViewModel", "onResponse")
                accessToken.value = response.body()!!.accessToken
                Log.i("LoginViewModel", response.body()!!.accessToken)
            }
        })
    }
}