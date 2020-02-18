package com.sktbd.driboard.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sktbd.driboard.R
import com.squareup.picasso.Picasso

class SmallShotsAdapter(private val linksList: List<String>) : RecyclerView.Adapter<SmallShotsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSmallShot = itemView.findViewById<ImageView>(R.id.ivSmallShot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.recycler_view_item_small_shot, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(linksList[position]).into(holder.ivSmallShot)
    }

    override fun getItemCount(): Int {
        return linksList.size
    }

}