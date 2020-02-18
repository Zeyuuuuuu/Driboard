package com.sktbd.driboard.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sktbd.driboard.R
import com.sktbd.driboard.data.models.Shot
import com.sktbd.driboard.databinding.ShotItemBinding
import com.sktbd.driboard.ui.viewmodel.ShotViewModel


class RcAdapter(var shotList:List<Shot>, var viewModel: ShotViewModel) : RecyclerView.Adapter<RcAdapter.VHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.shot_item,parent,false)
        return VHolder(v,viewModel)
    }

    override fun getItemCount(): Int {
        return shotList.size
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val shot: Shot =shotList[position]
        holder.bindView(shot)
    }

    inner class VHolder(itemView:View,var viewModel:ShotViewModel):RecyclerView.ViewHolder(itemView) {

        var lbinding: ShotItemBinding?=null
        init{
            lbinding=DataBindingUtil.bind(itemView)
        }
        fun bindView(shot: Shot){
            this.lbinding!!.shot = shot
            lbinding!!.executePendingBindings()
        }
    }
}

