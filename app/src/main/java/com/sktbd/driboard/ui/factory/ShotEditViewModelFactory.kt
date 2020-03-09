package com.sktbd.driboard.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel
import java.lang.IllegalArgumentException

class ShotEditViewModelFactory(private val accessToken: String,
                               private val state: Int,
                               private val shotId: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShotEditViewModel::class.java)) {
            return ShotEditViewModel(accessToken, state, shotId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}