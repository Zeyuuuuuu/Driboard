package com.sktbd.driboard.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.network.DriboardService
import com.sktbd.driboard.utils.Constants
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.HashMap

class ShotEditViewModel : ViewModel() {
    var title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val tags = MutableLiveData<ArrayList<String>>()

    fun onTitleChanged(newTitle:String?){
        title.value = newTitle
    }

    fun onDescriptionChanged(newDescription:String?){
        description.value = newDescription
    }

    fun onTagsChanged(tag:String?){
        if (tags.value == null){
            tags.value = ArrayList()
        }
        tags.value?.add(tag!!)
    }
    fun onTagsRemove(tag:String?){
        tags.value?.remove(tag)
    }

    fun hasTag(tag:String?):Boolean?{
        if (tags.value == null){
            return false
        }
        return tags.value?.contains(tag)
    }
    fun publish(){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val driboardService: DriboardService = retrofit.create(DriboardService::class.java)
//        val body = HttpUtils.createShotFilPart(
//            context,
//            draft.croppedImgDimen!!,
//            Uri.parse(draft.imageUri),
//            draft.imageFormat,
//            "image")
//        //add to HashMap key and RequestBody
//        val map = HashMap<String, RequestBody>()
//        driboardService.publishShot(map,body,title.value,description.value,tags.value).subscribe(
//            { response ->
//                when (response.code()) {
//                    Constants.ACCEPTED -> { onPublishSucceed(draft) }
//                    else ->
//                        onPublishFailed("Post failed: " + response.code() +": "+ response.message(), null)
//                }
//            },
//            {t -> onPublishFailed(t.toString(), t)}
    }

    fun save(){
        println(tags.value)

    }
}
