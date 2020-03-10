package com.sktbd.driboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
<<<<<<< HEAD
=======
import androidx.navigation.Navigation
>>>>>>> 4874ad523755ad3c9e224b5f9d98789eab2d4654
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sktbd.driboard.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_fragment.*

<<<<<<< HEAD
class SmallShotsAdapter(private val linksList: List<String>, val parentFragment: Fragment) : RecyclerView.Adapter<SmallShotsAdapter.ViewHolder>() {
=======
class SmallShotsAdapter(private val linksList: List<String>,val parentFragment: Fragment) : RecyclerView.Adapter<SmallShotsAdapter.ViewHolder>() {
>>>>>>> 4874ad523755ad3c9e224b5f9d98789eab2d4654

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSmallShot = itemView.findViewById<ImageView>(R.id.ivSmallShot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.recycler_view_item_small_shot, parent, false)
        v.setOnClickListener{
            parentFragment.findNavController().navigate(R.id.action_userFragment_to_shotBoardFragment)
        }
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(linksList[position]).into(holder.ivSmallShot)
    }

    override fun getItemCount(): Int {
        return linksList.size
    }

}