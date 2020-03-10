package com.sktbd.driboard.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.chip.Chip
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.FragmentShotDetailBinding
import com.sktbd.driboard.ui.activity.MainActivity
import com.sktbd.driboard.ui.factory.ShotDetailViewModelFactory
import com.sktbd.driboard.ui.viewmodel.ShotDetailViewModel
import com.sktbd.driboard.utils.Constants
import kotlinx.android.synthetic.main.fragment_shot_detail.*


class ShotDetailFragment : Fragment() {

    private lateinit var viewModel: ShotDetailViewModel
    private lateinit var binding: FragmentShotDetailBinding
    private lateinit var viewModelFactory: ShotDetailViewModelFactory
    private lateinit var mActivity: MainActivity
    var shotId = ""
    var accessToken = ""

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
        accessToken = loadData()
        val args = ShotDetailFragmentArgs.fromBundle(arguments!!)
        viewModelFactory = ShotDetailViewModelFactory(accessToken, args.shotId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShotDetailViewModel::class.java)
        mActivity.setSupportActionBar(binding.detailToolbar)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mActivity = activity as MainActivity
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shot_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shotEditFragment -> {
                findNavController().navigate(ShotDetailFragmentDirections.actionShotDetailFragmentToShotEditFragment(Constants.UPDATE_SHOT_STATE, shotId, accessToken))
                return true
            }
            R.id.delete -> {
                val dialog = deleteDialog()
                dialog.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getShotDetail()
        viewModel.shotInfo.observe(viewLifecycleOwner, Observer { newShotInfo ->
            binding.shotDescription.text = newShotInfo.description
            binding.collapsingToolbarLayout.title = newShotInfo.title
            shotId = newShotInfo.id.toString()


            DetailChipGroup?.removeAllViews()
            newShotInfo.tags.forEach {
                val chip = Chip(context)
                chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
                chip.text = it
                DetailChipGroup?.addView(chip as View)
            }
            val sharedPref = activity?.getSharedPreferences("networkInfo", Context.MODE_PRIVATE)
            val networkInfo: String =  sharedPref!!.getString("networkInfo", "")!!
            var imgResource = newShotInfo.images.normal

            if(networkInfo == Constants.WIFI && newShotInfo.images.hidpi != null)
                imgResource = newShotInfo.images.hidpi



            Glide.with(activity!!)
                .load(imgResource)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(shot_detail_image)
        })
    }


    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }

    override fun onResume() {
        super.onResume()
        val toolbar = activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val toolbar = activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
    }

    private fun deleteDialog(): AlertDialog {
        val myDialog = AlertDialog.Builder(context)
            .setTitle("Delete")
            .setMessage("Do you want to delete this shot?")
            .setPositiveButton("Delete") { dialog, which ->
                viewModel.deleteShot()
                dialog.dismiss();
             }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()
        return myDialog
    }
}
