package com.sktbd.driboard.ui.viewmodel

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.sktbd.driboard.data.model.AuthToken
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.network.DribbbleService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep


class Shot_RV_ViewModel : ViewModel() {

    var BASE_URL = "https://api.dribbble.com/v2/"
    var alMutableLiveData = MutableLiveData<List<Shot>>()

    fun getApiData()  {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service:DribbbleService = retrofit.create(DribbbleService::class.java)

        service.getShots("81f5cdf500bd8cf7ff934cea2200cf904b8c6dc2205829969fc602c2c76cb644").enqueue(object : Callback<List<Shot>> {
            override fun onResponse(call: Call<List<Shot>>, response: Response<List<Shot>>) {
                alMutableLiveData.value = response.body()
//                srViewModel.isLoading.set(false)
                Log.d("DATA","got")
            }
            override fun onFailure(call: Call<List<Shot>>, t: Throwable) {
                Log.d("DATA","failed")
            }
        })
    }

    fun clear() {
        alMutableLiveData.value = null
        Log.d("DATA","clear")
    }

    companion object {
        @BindingAdapter("app:imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String) = Glide.with(view.context)
            .load(url)
            .centerCrop()
            .into(view)
    }
}