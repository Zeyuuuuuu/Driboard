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
import com.sktbd.driboard.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ShotDetailViewModel(): ViewModel() {
//    private val id: Int = shot_id
//    private val token : String = accessToken
    private val _shotInfo = MutableLiveData<Shot>()
    val shotInfo: LiveData<Shot>
        get() = _shotInfo
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    fun getShotDetail() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val dribbbleService: DribbbleService = retrofit.create(DribbbleService::class.java)
        var call: Call<Shot> = dribbbleService.getShotById(10654771, Constants.ACCESS_TOKEN);
        call.enqueue(object: Callback<Shot> {
            override fun onFailure(call: Call<Shot>, t: Throwable) {
                Log.e("ShotsDetailViewModelGetUser",t.toString())
            }

            override fun onResponse(call: Call<Shot>, response: Response<Shot>) {
                _shotInfo.value = response.body() as Shot
                _imageUrl.value = _shotInfo.value!!.images.normal

            }
        })
    }



//    @BindingAdapter("bind:imageUrl")
//    fun loadImage(view: ImageView, imageUrl: String?) {
//        Picasso.get().load(imageUrl).into(view)
//    }
}