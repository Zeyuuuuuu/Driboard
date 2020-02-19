package com.sktbd.driboard.ui.viewmodel


import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.network.DribbbleService
import com.sktbd.driboard.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ShotViewModel : ViewModel() {

    var BASE_URL = "https://api.dribbble.com/v2/"
    var alMutableLiveData = MutableLiveData<List<Shot>>()

    fun getApiData() : MutableLiveData<List<Shot>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service:DribbbleService = retrofit.create(DribbbleService::class.java)

        service.getShots(Constants.ACCESS_TOKEN).enqueue(object : Callback<List<Shot>> {
            override fun onResponse(call: Call<List<Shot>>, response: Response<List<Shot>>) {
                alMutableLiveData.value = response.body()
            }
            override fun onFailure(call: Call<List<Shot>>, t: Throwable) {
                Log.d("DATA","failed")
            }
        })
        return alMutableLiveData
    }
    companion object {
        @BindingAdapter("app:imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String) = Glide.with(view.context).load(url).into(view)
    }
}