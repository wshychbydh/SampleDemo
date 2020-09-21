package com.cool.eye.func.permission

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatButton
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.checker.Request
import com.eye.cool.permission.checker.permissionForResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    GlobalScope.launch {
      val result = permissionForResult(
          arrayOf(
              android.Manifest.permission.READ_SMS,//manifest中未注册，授权不会成功
              android.Manifest.permission.WRITE_EXTERNAL_STORAGE
          )
      )
      Log.i("Denied permission", result.denied?.joinToString(" ; ") ?: "None")
      ToastHelper.showToast(requireContext(), "授权${result.isSucceed()}")
    }
  }
}