package com.sktbd.driboard.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sktbd.driboard.R
import com.sktbd.driboard.ui.viewmodel.ShotViewModel


class ShotFragment : Fragment() {

    companion object {
        fun newInstance() = ShotFragment()
    }

    private lateinit var viewModel: ShotViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shot_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShotViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
