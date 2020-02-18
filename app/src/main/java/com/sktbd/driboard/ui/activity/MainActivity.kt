package com.sktbd.driboard.ui.activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.R

import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.sktbd.driboard.databinding.ActivityMainBinding
import com.sktbd.driboard.ui.adapters.RcAdapter
import com.sktbd.driboard.ui.viewmodel.ShotViewModel


class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding?=null
    var rvAdapter: RcAdapter?=null
    var rcViewModel:ShotViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding!!.lifecycleOwner

        rcViewModel= ViewModelProvider(this@MainActivity).get(ShotViewModel::class.java)

        rcViewModel?.apply {
            getApiData()
            alMutableLiveData.observe(this@MainActivity,androidx.lifecycle.Observer {list ->
                if(list!==null){
                    rvAdapter= RcAdapter(list,rcViewModel!!)
                    binding!!.rv.layoutManager=LinearLayoutManager(this@MainActivity)
                    binding!!.rv.adapter=rvAdapter
                }
            })
        }


    }

}
