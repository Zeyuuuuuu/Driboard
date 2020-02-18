package com.sktbd.driboard.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.R

import androidx.recyclerview.widget.LinearLayoutManager
import com.sktbd.driboard.databinding.ActivityMainBinding
import com.sktbd.driboard.ui.adapters.RcAdapter
import com.sktbd.driboard.ui.viewmodel.ShotViewModel


class ShotsBoardActivity : AppCompatActivity() {
    var binding: ActivityMainBinding?=null
    var rvAdapter: RcAdapter?=null
    var rcViewModel:ShotViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding!!.lifecycleOwner

        rcViewModel= ViewModelProvider(this@ShotsBoardActivity).get(ShotViewModel::class.java)

        rcViewModel?.apply {
            getApiData()
            alMutableLiveData.observe(this@ShotsBoardActivity,androidx.lifecycle.Observer { list ->
                if(list!==null){
                    rvAdapter= RcAdapter(list,rcViewModel!!)
                    binding!!.rv.layoutManager=LinearLayoutManager(this@ShotsBoardActivity)
                    binding!!.rv.adapter=rvAdapter
                }
            })
        }


    }

}
