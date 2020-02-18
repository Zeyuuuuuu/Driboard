package com.sktbd.driboard.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.model.User
import com.sktbd.driboard.data.network.DriboardService
import com.sktbd.driboard.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserViewModel : ViewModel() {
    var userInfo = MutableLiveData<User>()
    val shotLinks = MutableLiveData<List<String>>()
    fun init(){
        userInfo.value = null
    }
    fun getUser(){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val driboardService:DriboardService  = retrofit.create(DriboardService::class.java)
        driboardService.getUser(Constants.ACCESS_TOKEN).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>,response: Response<User>){
                userInfo.value = (response.body() as User)
            }
            override fun onFailure(call: Call<User>,t: Throwable){
                Log.e("UserViewModelGetUser",t.toString())

            }

        })
        driboardService.getUserShots(Constants.ACCESS_TOKEN).enqueue(object : Callback<List<Shot>> {
            override fun onResponse(call: Call<List<Shot>>,response: Response<List<Shot>>){
                shotLinks.value = response.body()?.map{it.links.hidpi}
            }
            override fun onFailure(call: Call<List<Shot>>,t: Throwable){
                Log.e("UserViewModelGetShots",t.toString())

            }

        })
    }
}



