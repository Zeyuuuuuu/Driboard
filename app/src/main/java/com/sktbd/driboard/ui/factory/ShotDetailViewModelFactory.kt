package com.sktbd.driboard.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.ui.viewmodel.ShotDetailViewModel
import java.lang.IllegalArgumentException

class ShotDetailViewModelFactory(private val accessToken: String, private val shotId: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShotDetailViewModel::class.java)) {
            return ShotDetailViewModel(shotId, accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}