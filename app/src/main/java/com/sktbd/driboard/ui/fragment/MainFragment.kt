package com.sktbd.driboard.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var accessToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)

        accessToken = loadData()
        if (accessToken == "") {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
        } else {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToUserFragment(accessToken))
        }
        return binding.root
    }

    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }
}
