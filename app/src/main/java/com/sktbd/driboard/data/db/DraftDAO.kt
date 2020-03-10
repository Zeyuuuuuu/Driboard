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


    @Update
    fun update(draft: DraftEntity)

    @Insert
    fun insert(draft: DraftEntity)

    @Update
    fun update(draft: DraftEntity)

    @Delete
    fun delete(darft: DraftEntity)

    @Query("DELETE FROM drafts where draftID = :id")
    fun deleteById(id:String)
}