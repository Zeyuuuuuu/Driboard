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


class Shot_RV_ViewModel : ViewModel() {

    var BASE_URL = "https://api.dribbble.com/v2/"
    var alMutableLiveData = MutableLiveData<List<Shot>>()

    fun getApiData()  {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service:DribbbleService = retrofit.create(DribbbleService::class.java)

        service.getShots("5926ce05b97926551804f82426e846678ea96303bcdc1204a206863781dff7c5").enqueue(object : Callback<List<Shot>> {
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

    companion object {
        @BindingAdapter("app:imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String) = Glide.with(view.context)
            .load(url)
            .centerCrop()
            .into(view)
    }
}