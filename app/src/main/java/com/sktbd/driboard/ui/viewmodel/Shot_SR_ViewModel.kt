package com.sktbd.driboard.ui.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel


class Shot_SR_ViewModel : ViewModel(){
    var isLoading = ObservableBoolean()

    /* Needs to be public for Databinding */
    fun onRefresh() {
        isLoading.set(true)
    }

    fun onReady(results: List<*>?) {
        isLoading.set(false)
    }

    fun onError(oops: Exception?) {
        isLoading.set(false)
    }
}