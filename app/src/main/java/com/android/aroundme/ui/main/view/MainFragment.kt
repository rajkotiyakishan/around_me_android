package com.android.aroundme.ui.main.view

import android.content.Intent
import android.util.Log.e
import androidx.fragment.app.viewModels
import com.android.aroundme.CoreFragment
import com.android.aroundme.R
import com.android.aroundme.databinding.FragmentMainBinding
import com.android.aroundme.ui.main.viewmodel.MainViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import org.supportcompact.adapters.Page
import org.supportcompact.adapters.setFragmentPagerAdapter

@AndroidEntryPoint
class MainFragment : CoreFragment<FragmentMainBinding>() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

    override fun setVM(binding: FragmentMainBinding) {
        binding.vm = mainViewModel
        binding.executePendingBindings()
    }

    override fun createReference() {
        setUpTabLayout()
    }

    private fun setUpTabLayout() {
        val pages = arrayListOf(
            Page("", PlaceListFragment.newInstance()),
            Page("", PlaceMapFragment.newInstance())
        )

        getBinding().mPager.isNestedScrollingEnabled = false
        getBinding().mPager.offscreenPageLimit = 1
        getBinding().mPager.setFragmentPagerAdapter(childFragmentManager, pages)
        getBinding().mTab.setupWithViewPager(getBinding().mPager, true)

        getBinding().mTab.getTabAt(0)?.setIcon(R.drawable.ic_list)
        getBinding().mTab.getTabAt(1)?.setIcon(R.drawable.ic_map)

        mainViewModel.fetchPlaces("23.1978065,72.608824")

    }


}