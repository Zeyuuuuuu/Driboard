package com.sktbd.driboard.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.sktbd.driboard.data.db.AppDatabase
import com.sktbd.driboard.data.model.DraftEntity
import com.sktbd.driboard.ui.fragment.DraftListFragment


class DraftListViewModel(context: Context) : ViewModel() {

    var alMutableLiveData = MutableLiveData<List<DraftEntity>>()
    val db = Room.databaseBuilder(context, AppDatabase::class.java, "drafts").allowMainThreadQueries().build()

    fun getData()  {
        alMutableLiveData.value = db.draftDao().getAll()
    }

    fun addData(draft: DraftEntity)  {
        db.draftDao().insert(draft)
    }

    fun clear() {
        alMutableLiveData.value = emptyList()
        Log.d("DATA","clear")
    }

    fun delete(pos : Int){
        db.draftDao().delete(alMutableLiveData.value!![pos])
    }

//    companion object {
//        @BindingAdapter("app:imageUrl")
//        @JvmStatic
//        fun loadImage(view: ImageView, url: String) = Glide.with(view.context)
//            .load(url)
//            .centerCrop()
//            .into(view)
//    }
}