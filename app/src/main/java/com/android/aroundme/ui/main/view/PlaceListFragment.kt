package com.android.aroundme.ui.main.view

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.data.model.Places
import com.android.aroundme.databinding.FragmentPlaceListBinding
import com.android.aroundme.databinding.RowPlaceListBinding
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import com.android.aroundme.utils.Status
import com.android.aroundme.utils.adapter.setUpRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PlaceListFragment : CoreFragment<FragmentPlaceListBinding>() {
    var mainViewModel: MainViewModel? = null


    override fun getLayout(): Int {
        return R.layout.fragment_place_list
    }

    override fun setVM(binding: FragmentPlaceListBinding) {
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
                        setPlaceList(list)
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

    private fun setPlaceList(list: List<Places>) {
        getBinding().rvPlace.addItemDecoration(
            DividerItemDecoration(
                getBinding().rvPlace.context,
                DividerItemDecoration.VERTICAL
            )
        )

        getBinding().rvPlace.setUpRecyclerView(
            R.layout.row_place_list,
            list as ArrayList<Places>
        ) { item: Places, binder: RowPlaceListBinding, position ->
            binder.place = item
            binder.executePendingBindings()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PlaceListFragment().apply {}
    }

}