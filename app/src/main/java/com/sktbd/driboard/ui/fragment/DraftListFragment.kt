package com.sktbd.driboard.ui.fragment

import android.app.Application
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sktbd.driboard.data.db.AppDatabase
import com.sktbd.driboard.data.model.DraftEntity
import com.sktbd.driboard.databinding.DraftListFragmentBinding
import com.sktbd.driboard.ui.adapter.DraftList_RVAdapter
import com.sktbd.driboard.ui.adapter.OnItemClickListener
import com.sktbd.driboard.ui.factory.DraftListViewModelFactory
import com.sktbd.driboard.ui.viewmodel.DraftListViewModel
import com.sktbd.driboard.utils.Constants


class DraftListFragment : Fragment (), SwipeRefreshLayout.OnRefreshListener  {
    private lateinit var binding: DraftListFragmentBinding
    private lateinit var rvAdapter: DraftList_RVAdapter
    private lateinit var draftListViewModel: DraftListViewModel
    private lateinit var draftListViewModelFactory: DraftListViewModelFactory
    companion object {
        fun newInstance() = ShotBoardFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val accessToken = loadData()
        draftListViewModelFactory = DraftListViewModelFactory(activity!!.applicationContext)
        draftListViewModelFactory = DraftListViewModelFactory(context!!)

        draftListViewModel = ViewModelProvider(this, draftListViewModelFactory).get(DraftListViewModel::class.java)
        binding = DraftListFragmentBinding.inflate(inflater,container,false)
//        draftListViewModel.addData(DraftEntity("asdfsadfadsfasd",2,"DFSD","dasfadsfas","asdfa","asdfasdfas","asdfasf"))


        draftListViewModel.apply {
            draftListViewModel.getData()
            alMutableLiveData.observe(viewLifecycleOwner,androidx.lifecycle.Observer { list ->
                if(list!==null){
                    rvAdapter= DraftList_RVAdapter(list, draftListViewModel)
                    rvAdapter.setOnItemClickListener(object : OnItemClickListener {
                        override fun onclick(v: View, position: Int) {
                            Log.i("CLICK", list[position].id.toString())
                            findNavController().navigate(DraftListFragmentDirections.actionDraftListFragmentToShotEditFragment(
                                list[position].state, list[position].draftID, accessToken))
                        }
                    })

                    rvAdapter.setOnDeleteClickListener(object : OnItemClickListener {
                        override fun onclick(v: View, position: Int) {
                            Log.i("Delete", list[position].id.toString())
                            draftListViewModel.delete(position)
                            draftListViewModel.getData()
                        }
                    })
                    binding.rvDraftList.layoutManager= LinearLayoutManager(activity)
                    binding.rvDraftList.adapter = rvAdapter
                }
            })
        }

        binding.swipeContainerDraftList.setOnRefreshListener(this)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.findViewById<Toolbar>(com.sktbd.driboard.R.id.toolbar).title = "My Drafts"
    }

    override fun onRefresh() {
        draftListViewModel.clear()
        draftListViewModel.getData()
        binding.swipeContainerDraftList.isRefreshing = false
    }
    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }
}
