package com.android.aroundme

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.android.aroundme.utils.ktx.PermissionCallBack
import com.android.aroundme.utils.ktx.checkPermissionRationale
import com.android.aroundme.utils.ktx.checkSelfPermissions
import com.android.aroundme.utils.ktx.requestAllPermissions

abstract class CoreActivity< DB : ViewDataBinding> : AppCompatActivity() {

    private lateinit var binding: DB
    private val PERMISSION_CODE = 111
    private var permissionCallBack: PermissionCallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayout())
        createReference()
    }

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun createReference()

    fun getBinding(): DB = binding


    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)

    }


   /* protected fun requestPermissionsIfRequired(permissions: ArrayList<String>, permissionCallBack: PermissionCallBack?) {
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
    }*/


}
