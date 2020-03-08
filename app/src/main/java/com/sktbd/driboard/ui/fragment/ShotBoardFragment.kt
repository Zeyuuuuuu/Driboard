package com.sktbd.driboard.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.ShotBoardFragmentBinding
import com.sktbd.driboard.ui.adapter.OnItemClickListener
import com.sktbd.driboard.ui.adapter.RcAdapter
import com.sktbd.driboard.ui.factory.ShotRVViewModelFactory
import com.sktbd.driboard.ui.factory.UserViewModelFactory
import com.sktbd.driboard.ui.viewmodel.Shot_RV_ViewModel
import com.sktbd.driboard.ui.viewmodel.Shot_SR_ViewModel
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.delay
import java.lang.Thread.sleep

class ShotBoardFragment : Fragment (), SwipeRefreshLayout.OnRefreshListener  {
    private lateinit var binding: ShotBoardFragmentBinding
    private lateinit var rvAdapter: RcAdapter
    private lateinit var rcViewModel: Shot_RV_ViewModel
    private lateinit var srViewModel: Shot_SR_ViewModel
    private lateinit var shotRVViewModelFactory: ShotRVViewModelFactory
    companion object {
        fun newInstance() = ShotBoardFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        var args = ShotBoardFragmentArgs.fromBundle(arguments!!)
//
        val accessToken = loadData()
        shotRVViewModelFactory = ShotRVViewModelFactory(accessToken)
        rcViewModel= ViewModelProvider(this, shotRVViewModelFactory).get(Shot_RV_ViewModel::class.java)
        srViewModel= ViewModelProvider(this).get(Shot_SR_ViewModel::class.java)
        binding = ShotBoardFragmentBinding.inflate(inflater,container,false)
        rcViewModel.apply {
            getApiData()
            alMutableLiveData.observe(viewLifecycleOwner,androidx.lifecycle.Observer { list ->
                if(list!==null){
                    rvAdapter= RcAdapter(list, rcViewModel)
                    rvAdapter.setOnItemClickListener(object : OnItemClickListener {
                        override fun onclick(v: View, position: Int) {
                            Log.i("CLICK", list[position].id.toString())
                            this@ShotBoardFragment.findNavController().navigate(ShotBoardFragmentDirections.actionShotBoardFragmentToShotDetailFragment(list[position].id))
                        }
                    })
                    binding.rvShotBoard.layoutManager= GridLayoutManager(activity, 2)
                    binding.rvShotBoard.adapter=rvAdapter
                }
            })
        }

        binding.swipeContainer.setOnRefreshListener(this)



        return binding.root
    }

    override fun onRefresh() {
        rcViewModel.clear()
        rcViewModel.getApiData()
        binding.swipeContainer.isRefreshing = false
    }

    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }

}
