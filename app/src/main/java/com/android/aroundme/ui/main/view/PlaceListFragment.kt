package com.android.aroundme.ui.main.view

import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.aroundme.CoreApp
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.data.model.Places
import com.android.aroundme.databinding.FragmentPlaceListBinding
import com.android.aroundme.databinding.RowPlaceListBinding
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import com.android.aroundme.utils.Status
import com.android.aroundme.utils.adapter.setUpRecyclerView
import com.android.aroundme.utils.ktx.getLat
import com.android.aroundme.utils.ktx.getLong
import com.android.aroundme.utils.ktx.snackBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.logging.Handler


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
        getBinding().rvPlace.addItemDecoration(
            DividerItemDecoration(
                getBinding().rvPlace.context,
                DividerItemDecoration.VERTICAL
            )
        )
        setupObserver()
        getBinding().mRefresh.setOnRefreshListener {
            mainViewModel?.fetchPlaces(
                "${CoreApp.mInstance?.getLat()},${CoreApp.mInstance?.getLong()}")
            getBinding().mRefresh.isRefreshing = false
        }

    }

    private fun setupObserver() {
        mainViewModel?.places?.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    getBinding().shimmerLayout.stopShimmer()
                    getBinding().shimmerLayout.visibility = View.GONE
                    it.data?.let { list ->
                        setPlaceList(list)
                    }

                }
                Status.LOADING -> {
                    getBinding().rvPlace.visibility = View.GONE
                    getBinding().shimmerLayout.visibility = View.VISIBLE
                    getBinding().shimmerLayout.startShimmer()
                }
                Status.ERROR -> {
                    getBinding().shimmerLayout.stopShimmer()
                    getBinding().shimmerLayout.visibility = View.GONE
                    getBinding().mainCl.snackBar(it.message!!)
                }
            }
        })
    }

    private fun setPlaceList(list: List<Places>) {
        getBinding().rvPlace.visibility = View.VISIBLE
        getBinding().rvPlace.setUpRecyclerView(
            R.layout.row_place_list,
            list as ArrayList<Places>
        ) { item: Places, binder: RowPlaceListBinding, position ->
            binder.place = item
            binder.executePendingBindings()

            binder.ivDirection.setOnClickListener {
                findNavController().navigate(
                    R.id.placeDirectionFragment,
                    bundleOf("place" to item)
                )
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PlaceListFragment().apply {}
    }

}