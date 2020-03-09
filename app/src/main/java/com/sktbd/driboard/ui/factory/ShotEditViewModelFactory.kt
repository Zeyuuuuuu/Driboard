package com.sktbd.driboard.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel
import java.lang.IllegalArgumentException

class ShotEditViewModelFactory(private val accessToken: String,
                               private val isNew: Boolean,
                               private val shotId: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShotEditViewModel::class.java)) {
            return ShotEditViewModel(accessToken, isNew, shotId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}