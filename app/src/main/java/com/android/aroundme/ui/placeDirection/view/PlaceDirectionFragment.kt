package com.android.aroundme.ui.placeDirection.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.databinding.FragmentPlaceDirectionBinding
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDirectionFragment : CoreFragment<FragmentPlaceDirectionBinding>() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun getLayout(): Int {
        return R.layout.fragment_place_direction
    }

    override fun setVM(binding: FragmentPlaceDirectionBinding) {
        binding.vm = mainViewModel
        binding.executePendingBindings()

    }

    override fun createReference() {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaceDirectionFragment().apply {

            }
    }
}