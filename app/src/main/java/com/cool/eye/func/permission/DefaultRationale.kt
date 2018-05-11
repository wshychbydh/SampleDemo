package com.cool.eye.func.permission

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils

import com.cool.eye.func.R

/**
 * Created by cool on 2018/4/20.
 */
class DefaultRationale : Rationale {
    override fun showRationale(context: Context, permissions: Array<String>, callback: ((result: Boolean) -> Unit)?) {

        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames))

        AlertDialog.Builder(context).setCancelable(false)
                .setTitle(R.string.title_rationale).setMessage(message)
                .setPositiveButton(R.string.resume, { _, _ -> callback?.invoke(true) })
                .setNegativeButton(R.string.cancel, { _, _ -> callback?.invoke(false) })
                .show()
    }
}