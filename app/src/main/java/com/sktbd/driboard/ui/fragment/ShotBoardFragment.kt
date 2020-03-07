package com.sktbd.driboard.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.ShotBoardFragmentBinding
import com.sktbd.driboard.ui.adapter.RcAdapter
import com.sktbd.driboard.ui.viewmodel.Shot_RV_ViewModel
import com.sktbd.driboard.ui.viewmodel.Shot_SR_ViewModel
class ShotBoardFragment : Fragment() {
    private lateinit var binding: ShotBoardFragmentBinding
    private lateinit var rvAdapter: RcAdapter
    private lateinit var rcViewModel: Shot_RV_ViewModel
    private lateinit var srViewModel: Shot_SR_ViewModel
    companion object {
        fun newInstance() = ShotBoardFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rcViewModel= ViewModelProvider(this).get(Shot_RV_ViewModel::class.java)
        srViewModel= ViewModelProvider(this).get(Shot_SR_ViewModel::class.java)
        binding = ShotBoardFragmentBinding.inflate(inflater,container,false)
        rcViewModel.apply {
            getApiData()
            alMutableLiveData.observe(viewLifecycleOwner,androidx.lifecycle.Observer { list ->
                if(list!==null){
                    rvAdapter= RcAdapter(list, rcViewModel)
                    binding.rvShotBoard.layoutManager= GridLayoutManager(activity, 2)
                    binding.rvShotBoard.adapter=rvAdapter
                }
            })
        }
        return binding.root
    }

}
