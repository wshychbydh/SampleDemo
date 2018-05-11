package com.cool.eye.func.permission

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils

import com.cool.eye.func.R

/**
 * Created by cool on 2018/4/20.
 */
class SettingRationale :Rationale{

    override fun showRationale(context: Context, permissions: Array<String>, callback: ((result: Boolean) -> Unit)?) {

        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_always_failed,
                TextUtils.join("\n", permissionNames))

        val settingService = PermissionSetting(context)
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_rationale)
                .setMessage(message)
                .setPositiveButton(R.string.setting) { _, _ -> settingService.start() }
                .setNegativeButton(R.string.no) { _, _ -> settingService.cancel() }
                .show()
    }
}