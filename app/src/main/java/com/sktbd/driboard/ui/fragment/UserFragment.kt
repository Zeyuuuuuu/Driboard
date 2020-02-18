package com.sktbd.driboard.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sktbd.driboard.R
import com.sktbd.driboard.data.adapter.SmallShotsAdapter
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.ui.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_fragment.*


class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        viewModel.getUser()
        viewModel.userInfo.observe(
            viewLifecycleOwner,
            Observer {
                tvUsername.text = it.userName
                tvAddress.text = it.location
                tvTwitterLink.text = it.links?.twitter
                tvMydribbble.text = it.htmlUrl
                tvWebsite.text = it.links?.web
                tvBio.text = it.bio
                tvFollowers.text = it.followersCount.toString() + " Followers"
                Picasso.get().load(it.avatarUrl ).into(ivAvatar)
            })


        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvShots)
        recyclerView!!.layoutManager = LinearLayoutManager(view!!.context, RecyclerView.HORIZONTAL, false)

        viewModel.shotLinks.observe(
            viewLifecycleOwner,
            Observer {
                recyclerView.adapter = SmallShotsAdapter(it)
            })
        
    }


}
