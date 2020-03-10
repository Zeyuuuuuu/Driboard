package com.sktbd.driboard.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.ShotBoardFragmentBinding
import com.sktbd.driboard.ui.adapter.OnItemClickListener
import com.sktbd.driboard.ui.adapter.ShotBoard_RVAdapter
import com.sktbd.driboard.ui.factory.ShotRVViewModelFactory
import com.sktbd.driboard.ui.viewmodel.ShotBoardViewModel
import com.sktbd.driboard.utils.Constants
import kotlinx.android.synthetic.main.shot_board_fragment.view.*


class ShotBoardFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener  {
    private lateinit var binding: ShotBoardFragmentBinding
    private lateinit var rvAdapter: ShotBoard_RVAdapter
    private lateinit var rcViewModelBoard: ShotBoardViewModel
    private lateinit var shotRVViewModelFactory: ShotRVViewModelFactory
    companion object {
        fun newInstance() = ShotBoardFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        var args = ShotBoardFragmentArgs.fromBundle(arguments!!)

        val accessToken = loadData()
        shotRVViewModelFactory = ShotRVViewModelFactory(accessToken)
        rcViewModelBoard= ViewModelProvider(this, shotRVViewModelFactory).get(ShotBoardViewModel::class.java)
        binding = ShotBoardFragmentBinding.inflate(inflater,container,false)
        rcViewModelBoard.apply {
            getApiData()
            alMutableLiveData.observe(viewLifecycleOwner,androidx.lifecycle.Observer { list ->
                if(list!==null){
                    rvAdapter= ShotBoard_RVAdapter(list, rcViewModelBoard)
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

        activity!!.findViewById<Toolbar>(com.sktbd.driboard.R.id.toolbar).title = "My Shots"

        binding.swipeContainerShotBoard.setOnRefreshListener(this)
        binding.fab.setOnClickListener {
            this@ShotBoardFragment.findNavController().navigate(ShotBoardFragmentDirections.actionShotBoardFragmentToShotEditFragment(
                Constants.NEW_SHOT_STATE, "", accessToken))
        }

        return binding.root
    }

    override fun onRefresh() {
        rcViewModelBoard.clear()
        rcViewModelBoard.getApiData()
        binding.swipeContainerShotBoard.isRefreshing = false
    }

    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }

}
