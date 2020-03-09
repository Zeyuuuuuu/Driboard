package com.sktbd.driboard.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel
import java.lang.IllegalArgumentException

class ShotEditViewModelFactory(
    private val draftId: String?,
    private val state: Int,
    private val token:String
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShotEditViewModel::class.java)) {
            return ShotEditViewModel(draftId,state,token) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}