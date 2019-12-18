package com.cool.eye.func.permission

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatButton
import com.eye.cool.permission.PermissionHelper
import com.eye.cool.permission.support.Permission

class PermissionTestDialogFragment : AppCompatDialogFragment(), View.OnClickListener {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    val btn = AppCompatButton(requireContext())
    btn.text = "点击请求权限"
    btn.setOnClickListener(this)
    dialog.setContentView(btn)
    return dialog
  }

  override fun onClick(v: View?) {
    dismissAllowingStateLoss()
    PermissionHelper.Builder(this)
        .permissions(Permission.STORAGE)
        .showRationaleWhenRequest(true)
        .deniedPermissionCallback {
          it.forEach { i ->
            println("TestDialogFragment denied permission--->$i")
          }
        }
        .permissionCallback {
          Toast.makeText(context, "dialog request 授权$it", Toast.LENGTH_SHORT).show()
        }
        .build()
        .request()
  }
}