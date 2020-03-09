package com.sktbd.driboard.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.ui.viewmodel.DraftListViewModel

class DraftListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DraftListViewModel::class.java)) {
            return DraftListViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}