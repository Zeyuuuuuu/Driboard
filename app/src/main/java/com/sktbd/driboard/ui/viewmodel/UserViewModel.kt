package com.sktbd.driboard.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.model.User
import com.sktbd.driboard.data.network.DriboardService
import com.sktbd.driboard.data.network.RetrofitAPIManager
import com.sktbd.driboard.ui.fragment.UserFragment
import com.sktbd.driboard.ui.fragment.UserFragmentArgs
import com.sktbd.driboard.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserViewModel(accessToken: String) : ViewModel() {
    var userInfo = MutableLiveData<User>()
    val shotLinks = MutableLiveData<List<String>>()
    var token = ""
    val retrofitAPIManager = RetrofitAPIManager()
    val driboardService  = retrofitAPIManager.getDriboardService()

    init {
        token = accessToken
        Log.i("UserViewModel", token)
    }
    fun getUser(){


        driboardService.getUser(token).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>,response: Response<User>){
                Log.i("UserViewModel", response.body().toString())
                userInfo.value = (response.body() as User)
            }
            override fun onFailure(call: Call<User>,t: Throwable){
                Log.e("UserViewModelGetUser",t.toString())

            }
        })

        driboardService.getUserShots(token).enqueue(object : Callback<List<Shot>> {
            override fun onResponse(call: Call<List<Shot>>,response: Response<List<Shot>>){
                val links: List<String>? = response.body()?.map{it.images.normal}
                if (links != null) {
                    if (links.size > 4) {
                        shotLinks.value = links.slice(0..3)
                    } else {
                        shotLinks.value = links
                    }
                }

            }
            override fun onFailure(call: Call<List<Shot>>,t: Throwable){
                Log.e("UserViewModelGetShots",t.toString())

            }

        })
    }
}



