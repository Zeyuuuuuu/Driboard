package com.sktbd.driboard.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sktbd.driboard.R
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel


class ShotEditFragment : Fragment() {

    companion object {
        fun newInstance() = ShotEditFragment()
    }

    private lateinit var viewModel: ShotEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shot_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShotEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
