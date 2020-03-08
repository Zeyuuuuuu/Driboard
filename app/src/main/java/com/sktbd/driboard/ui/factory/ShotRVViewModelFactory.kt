package com.sktbd.driboard.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.ui.viewmodel.Shot_RV_ViewModel
import com.sktbd.driboard.ui.viewmodel.UserViewModel
import java.lang.IllegalArgumentException

class ShotRVViewModelFactory(private val accessToken: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Shot_RV_ViewModel::class.java)) {
            return Shot_RV_ViewModel(accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}