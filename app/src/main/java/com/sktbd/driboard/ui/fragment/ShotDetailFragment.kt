package com.sktbd.driboard.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.chip.Chip
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.FragmentShotDetailBinding
import com.sktbd.driboard.ui.factory.ShotDetailViewModelFactory
import com.sktbd.driboard.ui.viewmodel.ShotDetailViewModel
import kotlinx.android.synthetic.main.fragment_shot_detail.*
import kotlinx.android.synthetic.main.shot_edit_fragment.*


class ShotDetailFragment : Fragment() {

    private lateinit var viewModel: ShotDetailViewModel
    private lateinit var binding: FragmentShotDetailBinding
    private lateinit var viewModelFactory: ShotDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shot_detail,
            container,
            false
        )
        val accessToken = loadData()
        viewModelFactory = ShotDetailViewModelFactory(accessToken, 1000000)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShotDetailViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getShotDetail()
        viewModel.shotInfo.observe(viewLifecycleOwner, Observer { newShotInfo ->
            binding.shotDescription.setText(newShotInfo.description)
            binding.collapsingToolbarLayout.title = newShotInfo.title
            Glide.with(activity!!)
                .load(newShotInfo.images.normal)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(shot_detail_image);
            newShotInfo.tags.forEach {
                val chip = Chip(context)
                chip.text = it
                DetailChipGroup?.addView(chip as View)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }
}
