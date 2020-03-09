package com.sktbd.driboard.ui.viewmodel

import android.R
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.network.DribbbleService
import com.sktbd.driboard.data.network.RetrofitAPIManager
import com.sktbd.driboard.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ShotDetailViewModel(shotId: Int, accessToken: String): ViewModel() {
    private val id: Int = shotId
    private val _shotInfo = MutableLiveData<Shot>()
    val shotInfo: LiveData<Shot>
        get() = _shotInfo
    private val retrofitAPIManager = RetrofitAPIManager(accessToken)
    private val driboardService  = retrofitAPIManager.getDriboardService()
    init {
        Log.i("ShotDetailViewModel", shotId.toString())
    }

    fun getShotDetail() {
        var call: Call<Shot> = driboardService.getShotById(id);
        call.enqueue(object: Callback<Shot> {
            override fun onFailure(call: Call<Shot>, t: Throwable) {
                Log.e("ShotsDetailViewModelGetUser",t.toString())
            }

            override fun onResponse(call: Call<Shot>, response: Response<Shot>) {
                _shotInfo.value = response.body() as Shot
            }
        })
    }



//    @BindingAdapter("bind:imageUrl")
//    fun loadImage(view: ImageView, imageUrl: String?) {
//        Picasso.get().load(imageUrl).into(view)
//    }
}