package com.sktbd.driboard.ui.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.model.User
import com.sktbd.driboard.data.network.RetrofitAPIManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserViewModel(accessToken: String) : ViewModel() {
    var userInfo = MutableLiveData<User>()
    val shotLinks = MutableLiveData<List<String>>()
    private val retrofitAPIManager = RetrofitAPIManager(accessToken)
    private val driboardService  = retrofitAPIManager.getDriboardService()
    init {
        Log.i("UserViewModel", accessToken)
    }
    fun getUser(){
        driboardService.getUser().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>,response: Response<User>){
                Log.i("UserViewModel", response.body().toString())
                if (response.body() == null)
                    Log.e("UserViewModel", "accessToken invalid, maybe account has been suspended")
                else
                    userInfo.value = (response.body() as User)
            }
            override fun onFailure(call: Call<User>,t: Throwable){
                Log.e("UserViewModelGetUser",t.toString())
            }
        })

        driboardService.getUserShots().enqueue(object : Callback<List<Shot>> {
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



