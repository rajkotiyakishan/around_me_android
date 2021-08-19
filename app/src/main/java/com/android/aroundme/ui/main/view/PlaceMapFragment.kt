package com.android.aroundme.ui.main.view

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.databinding.FragmentPlaceMapBinding
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import com.android.aroundme.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceMapFragment : CoreFragment<FragmentPlaceMapBinding>() {
    private var mainViewModel: MainViewModel? = null

    override fun getLayout(): Int {
        return R.layout.fragment_place_map
    }

    override fun setVM(binding: FragmentPlaceMapBinding) {
        binding.vm = mainViewModel
        binding.executePendingBindings()
    }

    override fun createReference() {
        parentFragment?.let {
            mainViewModel =
                ViewModelProvider(it).get(MainViewModel::class.java)

        }
        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel?.users?.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    getBinding().progressBar.visibility = View.GONE
                    it.data?.let { list ->

                    }

                }
                Status.LOADING -> {
                    getBinding().progressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    getBinding().progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            PlaceMapFragment().apply {

            }
    }
}