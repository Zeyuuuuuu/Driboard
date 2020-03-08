package com.sktbd.driboard.ui.fragment

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
import com.google.android.material.chip.Chip
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.FragmentShotDetailBinding
import com.sktbd.driboard.ui.viewmodel.ShotDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.shot_edit_fragment.*


class ShotDetailFragment : Fragment() {

    private lateinit var viewModel: ShotDetailViewModel
    private lateinit var binding: FragmentShotDetailBinding
//    val imageView: ImageView = activity!!.findViewById(R.id.shot_detail_image)

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
        viewModel = ViewModelProvider(this).get(ShotDetailViewModel::class.java)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getShotDetail()
        viewModel.shotInfo.observe(viewLifecycleOwner, Observer { newShotInfo ->
            binding.shotDescription.setText(newShotInfo.description)
//            Picasso.get().load(newShotInfo.images.normal).into(imageView)
            newShotInfo.tags.forEach {
                val chip = Chip(context)
                chip.text = tag
                chipGroup?.addView(chip as View)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}
