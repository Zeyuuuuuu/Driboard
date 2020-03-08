package com.sktbd.driboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sktbd.driboard.R
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.databinding.ShotItemBinding
import com.sktbd.driboard.ui.viewmodel.Shot_RV_ViewModel
import kotlinx.android.synthetic.main.shot_item.view.*


class RcAdapter(var shotList:List<Shot>, var viewModel: Shot_RV_ViewModel) : RecyclerView.Adapter<RcAdapter.VHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

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

        if (onItemClickListener != null) {
            holder.itemView.imageView.setOnClickListener { onItemClickListener?.onclick(holder.itemView.imageView, position) }
        }
    }

    inner class VHolder(itemView:View,var viewModel:Shot_RV_ViewModel):RecyclerView.ViewHolder(itemView) {

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

