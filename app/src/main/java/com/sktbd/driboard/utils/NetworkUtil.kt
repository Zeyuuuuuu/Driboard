package com.sktbd.driboard.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService

class NetworkUtil {


    fun getConnectStatus(context: Context):String{
        val connectionManager:ConnectivityManager =  (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val network:Network = connectionManager.activeNetwork
        val networkCapabilities = connectionManager.getNetworkCapabilities(network!!)
        if(networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
            return Constants.WIFI
        }
        else if (networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
            return Constants.CELLULAR
        }
        return Constants.ERROR
    }


}