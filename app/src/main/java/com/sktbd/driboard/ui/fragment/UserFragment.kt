package com.sktbd.driboard.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.UserFragmentBinding
import com.sktbd.driboard.ui.adapter.SmallShotsAdapter
import com.sktbd.driboard.ui.factory.UserViewModelFactory
import com.sktbd.driboard.ui.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_fragment.*


class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var binding: UserFragmentBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var viewModelFactory: UserViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.user_fragment,
            container,
            false
        )

        binding.lyShots.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_userFragment_to_shotBoardFragment)
        )
        binding.rvShots.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_userFragment_to_shotBoardFragment)
        )
        activity!!.findViewById<Toolbar>(R.id.toolbar).title = "My Profile"

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var args = UserFragmentArgs.fromBundle(arguments!!)
        viewModelFactory = UserViewModelFactory(args.accessToken)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        viewModel.getUser()
        viewModel.userInfo.observe(
            viewLifecycleOwner,
            Observer {
                tvUsername.text = it.userName
                tvAddress.text = it.location
                tvTwitterLink.text = it.links?.twitter
                tvTwitterLink.setOnClickListener{jumpTo(tvTwitterLink.text.toString()!!)}
                tvMydribbble.text = it.htmlUrl
                tvMydribbble.setOnClickListener{jumpTo(tvMydribbble.text.toString()!!)}

                tvWebsite.text = it.links?.web
                tvWebsite.setOnClickListener{jumpTo(tvWebsite.text.toString()!!)}

                tvBio.text = it.bio
                tvFollowers.text = it.followersCount.toString() + " Followers"
                Picasso.get().load(it.avatarUrl ).into(ivAvatar)
            })


        rvShots.layoutManager = LinearLayoutManager(view!!.context, RecyclerView.HORIZONTAL, false)

        viewModel.shotLinks.observe(
            viewLifecycleOwner,
            Observer {
                rvShots.adapter = SmallShotsAdapter(it,this)
            })
        
    }

    private fun jumpTo(url:String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
