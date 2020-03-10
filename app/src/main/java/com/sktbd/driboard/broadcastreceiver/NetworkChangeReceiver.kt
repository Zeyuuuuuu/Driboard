package com.sktbd.driboard.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.sktbd.driboard.utils.NetworkUtil

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val networkInfo = NetworkUtil().getConnectStatus(context!!)
        Log.i("NetworkReceiver", networkInfo)
        val sharedPref: SharedPreferences.Editor = context?.getSharedPreferences("networkInfo", Context.MODE_PRIVATE)!!.edit()
        sharedPref.putString("networkInfo", networkInfo!!)
        sharedPref.apply()
    }
}