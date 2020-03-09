package com.sktbd.driboard.data.repository

import androidx.lifecycle.LiveData
import com.sktbd.driboard.data.db.DraftDao
import com.sktbd.driboard.data.model.Draft


//TODO
//class DraftRepository (private val dao: DraftDao) {
//
//    // Room executes all queries on a separate thread.
//    // Observed LiveData will notify the observer when the data has changed.
//    val alldrafts: LiveData<List<Draft>> = dao.getAll()
//
//    fun getAllDrafts(): LiveData<List<Draft>> {
//        return alldrafts
//    }
//    suspend fun insert(draft: Draft) {
//        dao.insert(draft)
//    }
//}