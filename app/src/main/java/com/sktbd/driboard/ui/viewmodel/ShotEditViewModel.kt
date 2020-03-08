package com.sktbd.driboard.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.network.DriboardService
import com.sktbd.driboard.utils.Constants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.ArrayList
import java.util.HashMap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import com.sktbd.driboard.data.model.Draft
import com.sktbd.driboard.data.model.User
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


class ShotEditViewModel : ViewModel() {
    val draft = MutableLiveData<Draft>()
    var isNew = false
    var isPending = MutableLiveData<Boolean>()
    var id = "10657904"


//    val title = MutableLiveData<String>()
//    val description = MutableLiveData<String>()
//    val tags = MutableLiveData<ArrayList<String>>()

    fun getShot(){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val driboardService:DriboardService  = retrofit.create(DriboardService::class.java)
        driboardService.getShot(Constants.ACCESS_TOKEN,id).enqueue(object : Callback<Draft> {
            override fun onResponse(call: Call<Draft>, response: Response<Draft>){
                Log.i("ShotEditViewModel getShotSuccess", response.body().toString())
                draft.value = (response.body() as Draft)
            }
            override fun onFailure(call: Call<Draft>, t: Throwable){
                Log.e("ShotEditViewModel getShotFail",t.toString())

            }
        })
    }

    fun onTitleChanged(newTitle:String){
        draft.value!!.title = newTitle
    }

    fun onDescriptionChanged(newDescription:String){
        draft.value!!.description = newDescription
    }

    fun onTagsChanged(tag:String?){
        draft.value!!.tags!!.add(tag!!)
    }
    fun onTagsRemove(tag:String?){
        draft.value!!.tags!!.remove(tag)
    }

    fun hasTag(tag:String?):Boolean?{
        return draft.value!!.tags!!.contains(tag)
    }
    fun publish(context: Context?, currentImgUri:String?){
        isPending.value = true
        val reSizefile = resizeImage(context,currentImgUri)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/png"), reSizefile)

        val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("title",draft.value?.title)
            .addFormDataPart("image",reSizefile.name,requestBody)
        if (draft.value?.description != null){
            requestBodyBuilder.addFormDataPart("description",draft.value?.description)
        }
        if (draft.value?.tags != null){
            val tagsList:ArrayList<String>? = draft.value?.tags
            Log.i("taglist",tagsList.toString().substring(1,tagsList.toString().length-1))
            requestBodyBuilder.addFormDataPart("tags",
                tagsList.toString().substring(1,tagsList.toString().length-1))
        }
        Log.i("uri",currentImgUri!!)
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val driboardService: DriboardService = retrofit.create(DriboardService::class.java)
        driboardService.publishShot(Constants.ACCESS_TOKEN,requestBodyBuilder.build())
            .enqueue(object: Callback<Response<Void>>{
                override fun onResponse(
                    call: Call<Response<Void>>,
                    response: Response<Response<Void>>
                ) {

                    Log.i("CODE", response.code().toString())
                    isPending.value = false

                }

                override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                    Log.i("Throwable", t.toString())
                    isPending.value = false

                }
            }
        )

    }
    fun update(){
        isPending.value = true
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val driboardService: DriboardService = retrofit.create(DriboardService::class.java)
        val tagsList:ArrayList<String>? = draft.value?.tags

        driboardService.updateShot(Constants.ACCESS_TOKEN,draft.value?.id!!,draft.value!!.title!!,draft.value!!.description,tagsList.toString().substring(1,tagsList.toString().length-1))
            .enqueue(object: Callback<Response<Void>>{
                override fun onResponse(
                    call: Call<Response<Void>>,
                    response: Response<Response<Void>>
                ) {
                    Log.i("CODE", response.toString())
                    isPending.value = false

                }

                override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                    Log.i("Throwable", t.toString())
                    isPending.value = false

                }
            }
            )

    }

    fun save(){
        println(draft.value!!.tags)
    }
    fun resizeImage(context:Context?,currentImgUri: String?):File{
        val f = File(context?.cacheDir,Uri.parse(currentImgUri).lastPathSegment)
        f.createNewFile()
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = false
        val bitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeFile(currentImgUri!!, bmOptions), 400, 300, true)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()
        val fos =  FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        bitmap.recycle()
        return f
    }
}
