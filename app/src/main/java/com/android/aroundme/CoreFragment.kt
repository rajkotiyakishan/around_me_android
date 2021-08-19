package com.android.aroundme

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.android.aroundme.utils.ktx.PermissionCallBack
import com.android.aroundme.utils.ktx.checkPermissionRationale
import com.android.aroundme.utils.ktx.checkSelfPermissions
import com.android.aroundme.utils.ktx.requestAllPermissions


abstract class CoreFragment< DB : ViewDataBinding> : androidx.fragment.app.Fragment() {
    private lateinit var binding: DB
    private var reference = false
    private val PERMISSION_CODE = 101
    private var permissionCallBack: PermissionCallBack? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)

            setVM(binding)
        } else {
            val v = binding.root.parent as ViewGroup?
            v?.removeView(binding.root)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!reference) {
            createReference()
            reference = true
        }
    }

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun setVM(binding: DB)

    abstract fun createReference()

    fun getBinding(): DB = binding


    protected fun requestPermissionsIfRequired(permissions: ArrayList<String>, permissionCallBack: PermissionCallBack?) {
        this.permissionCallBack = permissionCallBack
        if (checkSelfPermissions(permissions)) {
            permissionCallBack?.permissionGranted()
        } else {
            requestAllPermissions(permissions, PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.none { it != PackageManager.PERMISSION_GRANTED }) {
                    permissionCallBack?.permissionGranted()
                } else {
                    if (checkPermissionRationale(permissions)) {
                        permissionCallBack?.permissionDenied()
                    } else {
                        permissionCallBack?.onPermissionDisabled()
                    }
                }
            }
        }
    }

    @Deprecated("This method should not use with onBackPressedDispatcher", ReplaceWith("onBack(steps)"))
    protected fun onBack() {
        activity?.onBackPressed()
    }

    protected fun onBackExclusive(fragment: Class<out androidx.fragment.app.Fragment>) {
        activity?.supportFragmentManager?.popBackStackImmediate(fragment.name, 0)
    }

    protected fun onBackInclusive(fragment: Class<out androidx.fragment.app.Fragment>) {
        activity?.supportFragmentManager?.popBackStackImmediate(fragment.name, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    protected fun onBack(@IntRange(from = 1, to = 100) steps: Int) {
        for (i in 1..steps) {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

}