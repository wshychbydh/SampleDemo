package com.cool.eye.func.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi

/**
 * The permissions for all requests must be declared in the manifest.
 * Created by cool on 2018/4/13.
 */
class PermissionHelper private constructor(private var context: Context) {

    var rationale: Rationale? = null
    var rationaleSetting: Rationale? = null
    var callback: ((authorise: Boolean) -> Unit)? = null
    var permissions: Array<String>? = null

    fun request() {
        if (permissions == null || permissions!!.isEmpty()) {
            callback?.invoke(true)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission(context)
        } else {
            callback?.invoke(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission(context: Context) {
        //Some permission need request.
        val checkPermission = getRequestPermission(context, permissions)
        if (checkPermission.isEmpty()) {
            callback?.invoke(true)
        } else {
            var showRationale = false
            checkPermission.forEach {
                showRationale = showRationale || isNeedShowRationalePermission(context, it)
            }
            if (showRationale) {
                rationale?.showRationale(context, checkPermission, {
                    if (it) {
                        PermissionActivity.requestPermission(context, permissions!!, { requestPermissions, grantResults ->
                            verifyPermissions(requestPermissions, grantResults)
                        })
                    } else {
                        callback?.invoke(false)
                    }
                })

            } else {
                PermissionActivity.requestPermission(context, permissions!!, { requestPermissions, grantResults ->
                    verifyPermissions(requestPermissions, grantResults)
                })
            }
        }
    }


    private fun verifyPermissions(permissions: Array<String>, grantResults: IntArray) {

        // Verify that each required permission has been granted, otherwise all granted
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                if (hasAlwaysDeniedPermission(permissions)) {
                    rationaleSetting?.showRationale(context, permissions, null)
                } else {
                    callback?.invoke(false)
                }
                return
            }
        }
        callback?.invoke(true)
    }

    /**
     * Has always been denied permission.
     */
    private fun hasAlwaysDeniedPermission(deniedPermissions: Array<String>): Boolean {
        for (permission in deniedPermissions) {
            if (!isNeedShowRationalePermission(context, permission)) {
                return true
            }
        }
        return false
    }

    class Builder(private var context: Context) {
        private var rationale: Rationale? = null
        private var rationaleSetting: Rationale? = null
        private var callback: ((authorise: Boolean) -> Unit)? = null
        private var permissions: Array<String>? = null

        fun permission(permissions: Array<String>?): Builder {
            this.permissions = permissions
            return this
        }

        fun permissionCallback(callback: ((authorise: Boolean) -> Unit)? = null): Builder {
            this.callback = callback
            return this
        }

        fun rationale(rationale: Rationale): Builder {
            this.rationale = rationale
            return this
        }

        fun rationaleSetting(rationaleSetting: Rationale): Builder {
            this.rationaleSetting = rationaleSetting
            return this
        }

        fun build(): PermissionHelper {
            val permissionHelper = PermissionHelper(context)
            permissionHelper.permissions = permissions
            permissionHelper.callback = callback
            permissionHelper.rationale = rationale
            permissionHelper.rationaleSetting = rationaleSetting
            return permissionHelper
        }
    }

    companion object {

        private fun isNeedShowRationalePermission(context: Context, permission: String): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
            val packageManager = context.packageManager
            val pkManagerClass = packageManager.javaClass
            return try {
                val method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String::class.java)
                if (!method.isAccessible) method.isAccessible = true
                method.invoke(packageManager, permission) as Boolean? ?: false
            } catch (ignored: Exception) {
                false
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun getRequestPermission(context: Context, permissions: Array<String>?): Array<String> {
            val requestList = mutableListOf<String>()
            permissions?.forEach {
                if (context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                    requestList.add(it)
                }
            }
            return requestList.toTypedArray()
        }
    }
}