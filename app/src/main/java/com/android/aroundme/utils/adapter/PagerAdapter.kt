package org.supportcompact.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentStatePagerAdapter



/**
 * This is extension function setup the of ViewPager's PagerAdapter.
 * @param layout layout to be bound to adapter.
 * @param items data to be bound with layout.
 * @param onBind Is Unit function to override the  instantiateItem of PagerAdapter.
 * */
fun <T, U : ViewDataBinding> androidx.viewpager.widget.ViewPager.setPageAdapter(@LayoutRes layout: Int, items: ArrayList<T>, onBind: (binder: U, item: T) -> Unit): androidx.viewpager.widget.PagerAdapter? {
    adapter = object : androidx.viewpager.widget.PagerAdapter() {
        private var mCurrentPosition = -1

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val item: T = items[position]
            val binder: U = DataBindingUtil.bind(container.inflate(layout))!!
            container.addView(binder.root)
            onBind.invoke(binder, item)
            return binder.root
        }

        override fun isViewFromObject(view: View, `object`: Any) = view == `object`

        override fun getCount() = items.size

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return items[position].toString()
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }
    return adapter
}


/**
 * This is extension function setup the of ViewPager's FragmentPagerAdapter.
 * @param fm Is fragment manager.
 * @param fragments is list of fragment to be show.
 * */
fun androidx.viewpager.widget.ViewPager.setFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager, pages: ArrayList<Page>): androidx.fragment.app.FragmentPagerAdapter? {
    adapter = object : androidx.fragment.app.FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = pages[position].page
        override fun getCount() = pages.size
        override fun getPageTitle(position: Int) = pages[position].title
    }
    return adapter as androidx.fragment.app.FragmentPagerAdapter
}


/**
 * This is extension function setup the of ViewPager's FragmentPagerAdapter.
 * @param fm Is fragment manager.
 * @param fragments is list of fragment to be show.
 * */
fun androidx.viewpager.widget.ViewPager.setFragmentStatePagerAdapter(fm: androidx.fragment.app.FragmentManager, pages: ArrayList<Page>): androidx.fragment.app.FragmentPagerAdapter? {
    adapter = object : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = pages[position].page
        override fun getCount() = pages.size
        override fun getPageTitle(position: Int) = pages[position].title
    }
    return adapter as androidx.fragment.app.FragmentPagerAdapter
}

data class Page(var title: String, var page: androidx.fragment.app.Fragment)

infix fun ViewGroup.inflate(@LayoutRes lyt: Int) = LayoutInflater.from(context).inflate(lyt, this, false)!!