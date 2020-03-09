package com.sktbd.driboard.data.db

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sktbd.driboard.data.model.DraftEntity

@Dao
interface DraftDao {
    @Query("SELECT * FROM drafts")
    fun getAll(): List<DraftEntity>

    @Query("SELECT * FROM drafts where draftID = :id")
    fun getById(id:String): DraftEntity

    @Insert
    fun insert(draft: DraftEntity)

    @Delete
    fun delete(darft: DraftEntity)
}