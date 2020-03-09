package com.sktbd.driboard.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sktbd.driboard.data.model.Draft
import com.sktbd.driboard.data.network.RetrofitAPIManager
import com.sktbd.driboard.utils.Constants
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


class ShotEditViewModel(accessToken: String, isNew: Boolean, id : String?) : ViewModel() {
    val draft = MutableLiveData<Draft>()
    var state = Constants.NEW_SHOT_STATE
    var isNew = isNew
    var isPending = MutableLiveData<Boolean>()
    var id = id
    private val retrofitAPIManager = RetrofitAPIManager(accessToken)
    private val driboardService  = retrofitAPIManager.getDriboardService()


//    val title = MutableLiveData<String>()
//    val description = MutableLiveData<String>()
//    val tags = MutableLiveData<ArrayList<String>>()

    fun getShot(){

        when (state){
            Constants.NEW_SHOT_STATE -> {
                draft.value = Draft(id="",title = "",description = "",tags = ArrayList(),images = Draft.ImageUrl(""),imageUri = "")
            Constants.UPDATE_SHOT_STATE -> {
                driboardService.getShot(id!!).enqueue(object : Callback<Draft> {
                    override fun onResponse(call: Call<Draft>, response: Response<Draft>){
                        Log.i("ShotEditViewModel getShotSuccess", response.body().toString())
                        draft.value = (response.body() as Draft)
                    }
                    override fun onFailure(call: Call<Draft>, t: Throwable){
                        Log.e("ShotEditViewModel getShotFail",t.toString())

                    }
                })
            }
            Constants.NEW_DRAFT_STATE -> {

            }
            Constants.UPDATE_DRAFT_STATE -> {

            }
        }

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

    fun onPicUpload(currentImgUri:String){
        draft.value!!.imageUri = currentImgUri
    }

    fun publish(context: Context?){
        isPending.value = true
        val reSizefile = resizeImage(context)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/png"), reSizefile)

        val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("title",draft.value?.title!!)
            .addFormDataPart("image",reSizefile.name,requestBody)
        if (draft.value?.description != null){
            requestBodyBuilder.addFormDataPart("description",draft.value?.description!!)
        }
        if (draft.value?.tags != null){
            val tagsList:ArrayList<String>? = draft.value?.tags
            Log.i("taglist",tagsList.toString().substring(1,tagsList.toString().length-1))
            requestBodyBuilder.addFormDataPart("tags",
                tagsList.toString().substring(1,tagsList.toString().length-1))
        }
        Log.i("uri",draft.value!!.imageUri!!)

        driboardService.publishShot(requestBodyBuilder.build())
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

        val tagsList:ArrayList<String>? = draft.value?.tags

        driboardService.updateShot(draft.value?.id!!,draft.value!!.title,draft.value!!.description,tagsList.toString().substring(1,tagsList.toString().length-1))
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
    private fun resizeImage(context:Context?):File{
        val f = File(context?.cacheDir,Uri.parse(draft.value!!.imageUri).lastPathSegment!!)
        f.createNewFile()
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inJustDecodeBounds = false
        val bitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeFile(draft.value!!.imageUri!!, bitmapOption), 400, 300, true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, byteArrayOutputStream)
        val bitmapData = byteArrayOutputStream.toByteArray()
        val fileOutputStream =  FileOutputStream(f)
        fileOutputStream.write(bitmapData)
        fileOutputStream.flush()
        fileOutputStream.close()
        bitmap.recycle()
        return f
    }
}
