package com.sktbd.driboard.data.db

import androidx.lifecycle.MutableLiveData
import androidx.room.*
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

    @Update
    fun update(data: DraftEntity)
}