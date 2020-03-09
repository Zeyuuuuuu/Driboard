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
import com.sktbd.driboard.data.network.RetrofitAPIManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep


class ShotBoardViewModel(accessToken: String) : ViewModel() {

    var alMutableLiveData = MutableLiveData<List<Shot>>()
    private val retrofitAPIManager = RetrofitAPIManager(accessToken)
    private val driboardService = retrofitAPIManager.getDriboardService()

    fun getApiData()  {
        driboardService.getShots().enqueue(object : Callback<List<Shot>> {
            override fun onResponse(call: Call<List<Shot>>, response: Response<List<Shot>>) {
                alMutableLiveData.value = response.body()
                Log.i("Shot_RV_ViewModel", response.body().toString())
                Log.d("DATA","got")
            }
            override fun onFailure(call: Call<List<Shot>>, t: Throwable) {
                Log.d("DATA","failed")
            }
        })
    }

    fun clear() {
        alMutableLiveData.value = emptyList()
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